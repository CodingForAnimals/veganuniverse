package org.codingforanimals.veganuniverse.place.data.source

import android.os.Parcelable
import android.util.Log
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.firebase.storage.model.ResizeResolution
import org.codingforanimals.veganuniverse.firebase.storage.usecase.UploadPictureUseCase
import org.codingforanimals.veganuniverse.place.data.mapper.PlaceEntityMapper
import org.codingforanimals.veganuniverse.place.data.model.PlaceCardDatabaseEntity
import org.codingforanimals.veganuniverse.place.data.model.PlaceFirestoreEntity
import org.codingforanimals.veganuniverse.place.model.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.place.model.Place
import org.codingforanimals.veganuniverse.place.model.PlaceCard

internal class PlaceFirebaseDataSource(
    private val geoFireReference: DatabaseReference,
    private val cardsReference: DatabaseReference,
    private val placesCollection: CollectionReference,
    private val placesReportsReference: DatabaseReference,
    private val uploadPictureUseCase: UploadPictureUseCase,
    private val mapper: PlaceEntityMapper,
) : PlaceRemoteDataSource {

    override suspend fun getById(id: String): Place? {
        return placesCollection.document(id).get().await()
            .toObject(PlaceFirestoreEntity::class.java)
            ?.let { mapper.mapPlace(it) }
    }

    override suspend fun getByLatLng(latitude: Double, longitude: Double): Place? {
        val geoHash = GeoFireUtils.getGeoHashForLocation(GeoLocation(latitude, longitude))
        return placesCollection.document(geoHash).get().await()
            .toObject(PlaceFirestoreEntity::class.java)
            ?.let { mapper.mapPlace(it) }
    }

    override suspend fun queryCardsByGeoLocation(params: GeoLocationQueryParams): List<PlaceCard> {
        val cardsFlow = callbackFlow {
            val deferred = mutableMapOf<GeoLocation, Deferred<DataSnapshot>>()
            GeoFire(geoFireReference).queryAtLocation(
                /* center = */ GeoLocation(params.latitude, params.longitude),
                /* radius = */ params.radiusKm,
            ).addGeoQueryEventListener(
                object : GeoQueryEventListener {
                    override fun onKeyExited(key: String?) = Unit
                    override fun onKeyMoved(key: String?, location: GeoLocation?) = Unit

                    override fun onKeyEntered(key: String?, location: GeoLocation?) {
                        if (key != null && location != null) {
                            val card = cardsReference.child(key).get().asDeferred()
                            deferred[location] = card
                        }
                    }

                    override fun onGeoQueryReady() {
                        trySend(deferred)
                        channel.close()
                    }

                    override fun onGeoQueryError(error: DatabaseError?) {
                        Log.e(TAG, error?.message ?: "Error geoquerying for places")
                        Log.e(TAG, error?.details ?: "Error geoquerying for places")
                        channel.close()
                    }
                }
            )
            awaitClose()
        }
        return cardsFlow.firstOrNull()?.let { map: Map<GeoLocation, Deferred<DataSnapshot>> ->
            map.mapNotNull { entry ->
                val geoHash = entry.value.await().key ?: return@mapNotNull null
                val entity = entry.value.await().getValue(PlaceCardDatabaseEntity::class.java)
                    ?: return@mapNotNull null
                val location = entry.key
                mapper.mapCard(
                    geoHash = geoHash,
                    location = location,
                    entity = entity,
                )
            }
        } ?: emptyList()
    }

    override suspend fun insertPlace(place: Place, imageModel: Parcelable): String {
        val pictureId = uploadPictureUseCase(
            fileFolderPath = BASE_PLACE_PICTURE_PATH,
            model = imageModel
        )

        val resizedPictureId = pictureId + ResizeResolution.MEDIUM.suffix

        val geoHash =
            GeoFireUtils.getGeoHashForLocation(GeoLocation(place.latitude, place.longitude))

        return coroutineScope {
            val cardDeferred =
                cardsReference.child(geoHash).setValue(place.toNewCard(resizedPictureId))
                    .asDeferred()
            val placeDeferred =
                placesCollection.document(geoHash).set(place.toNewPlace(resizedPictureId))
                    .asDeferred()
            val geoFireCompletionSource = TaskCompletionSource<Void>()
            GeoFire(geoFireReference).setLocation(
                /* key = */ geoHash,
                /* location = */ GeoLocation(place.latitude, place.longitude)
            )
            /* completionListener = */ { _, error ->
                if (error != null) {
                    Log.e(TAG, "Error message: ${error.message}. Details: ${error.details}")
                }
                geoFireCompletionSource.setResult(null)
            }

            awaitAll(cardDeferred, placeDeferred, geoFireCompletionSource.task.asDeferred())
            geoHash
        }
    }

    override suspend fun reportPlace(placeId: String, userId: String) {
        placesReportsReference.child(placeId)
            .setValue(userId to true)
            .await()
    }

    override suspend fun deleteById(id: String) {
        coroutineScope {
            val removeCardDeferred = cardsReference.child(id).removeValue().asDeferred()
            val removePlaceDeferred = placesCollection.document(id).delete().asDeferred()
            val removeGeoFireDeferred = geoFireReference.child(id).removeValue().asDeferred()
            awaitAll(removeCardDeferred, removePlaceDeferred, removeGeoFireDeferred)
        }
    }

    private fun Place.toNewCard(imageId: String): PlaceCardDatabaseEntity {
        fun getAdministrativeArea(): String? {
            return addressComponents?.let {
                it.locality?.ifEmpty {
                    it.secondaryAdminArea?.ifEmpty {
                        it.primaryAdminArea?.ifEmpty { it.country }
                    }
                }
            }
        }
        return PlaceCardDatabaseEntity(
            name = name,
            rating = 0.0,
            streetAddress = addressComponents?.streetAddress,
            administrativeArea = getAdministrativeArea(),
            type = type?.name,
            tags = tags?.map { it.name },
            imageId = imageId,
        )
    }

    private fun Place.toNewPlace(imageId: String): PlaceFirestoreEntity {
        return PlaceFirestoreEntity(
            geoHash = geoHash,
            userId = userId,
            username = username,
            name = name?.trim(),
            nameLowercase = name?.trim()?.lowercase(),
            description = description,
            rating = 0.0,
            type = type?.name,
            tags = tags?.map { it.name },
            latitude = latitude,
            longitude = longitude,
            openingHours = openingHours,
            addressComponents = addressComponents,
            imageId = imageId,
            createdAt = null,
        )
    }

    companion object {
        private const val TAG = "PlaceFirebaseDataSource"
        internal const val PLACES_GEOFIRE = "content/places/geofire"
        internal const val PLACES_CARDS = "content/places/cards"
        internal const val PLACES_ITEMS_COLLECTION = "content/places/items"
        internal const val PLACES_REPORTS = "content/places/reports"
        internal const val BASE_PLACE_PICTURE_PATH = "content/places/picture"
    }
}