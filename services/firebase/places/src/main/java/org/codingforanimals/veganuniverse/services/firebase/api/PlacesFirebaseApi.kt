package org.codingforanimals.veganuniverse.services.firebase.api

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.entity.PlaceForm
import org.codingforanimals.veganuniverse.services.firebase.DatabasePath
import org.codingforanimals.veganuniverse.services.firebase.FirebaseImageResizer
import org.codingforanimals.veganuniverse.services.firebase.FirestoreCollection
import org.codingforanimals.veganuniverse.services.firebase.entity.Place
import org.codingforanimals.veganuniverse.services.firebase.entity.PlaceCard
import org.codingforanimals.veganuniverse.services.firebase.model.GetPlaceDeferred
import org.codingforanimals.veganuniverse.services.firebase.model.GetPlaceResult
import org.codingforanimals.veganuniverse.services.firebase.utils.createGeoHash
import org.codingforanimals.veganuniverse.services.firebase.utils.geoQuery
import org.codingforanimals.veganuniverse.places.entity.Place as PlaceDomainEntity
import org.codingforanimals.veganuniverse.places.entity.PlaceCard as PlaceCardDomainEntity


private const val TAG = "PlacesFirebaseApi"

internal class PlacesFirebaseApi(
    private val firestore: FirebaseFirestore,
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage,
    private val placeFormToPlaceMapper: OneWayEntityMapper<PlaceForm, Place>,
    private val placeFormToPlaceCardMapper: OneWayEntityMapper<PlaceForm, PlaceCard>,
    private val placeToDomainEntityMapper: OneWayEntityMapper<Place, PlaceDomainEntity>,
    private val getPlaceResultToPlaceCardMapper: OneWayEntityMapper<GetPlaceResult, PlaceCardDomainEntity>,
) : PlacesApi {

    override suspend fun fetchPlaces(params: GeoLocationQueryParams): List<PlaceCardDomainEntity> {
        val cbf = callbackFlow {
            // limit loading cards to 100 tops
            val getPlacesDeferredList = mutableListOf<GetPlaceDeferred>()
            database
                .getReference(DatabasePath.Content.Places.GEO_FIRE)
                .geoQuery(
                    params = params,
                    onKeyFound = { key, geoLocation ->
                        if (getPlacesDeferredList.size >= 100) return@geoQuery
                        val deferred = database
                            .getReference("${DatabasePath.Content.Places.CARDS}/$key")
                            .get().asDeferred()
                        getPlacesDeferredList.add(GetPlaceDeferred(geoLocation, deferred))
                    },
                    onGeoQueryDone = {
                        trySend(getPlacesDeferredList)
                        channel.close()
                    },
                    onGeoQueryError = {
                        Log.e(TAG, it?.stackTraceToString() ?: "Error geoquerying for places")
                        channel.close()
                    },
                )
            awaitClose()
        }

        val getPlacesDeferredList = cbf.first()
        return mapToPlaceCards(getPlacesDeferredList)
    }

    private suspend fun mapToPlaceCards(deferredList: List<GetPlaceDeferred>): List<PlaceCardDomainEntity> {
        val results = deferredList.map { it.await() }
        return results.mapNotNull { result ->
            try {
                getPlaceResultToPlaceCardMapper.map(result)
            } catch (e: Throwable) {
                Log.e(TAG, e.stackTraceToString())
                null
            }
        }
    }

    override suspend fun fetchPlace(
        latitude: Double,
        longitude: Double,
    ): PlaceDomainEntity? {
        val geoHash = createGeoHash(latitude, longitude)
        return fetchPlaceByGeoHashId(geoHash)
    }

    override suspend fun fetchPlace(geoHash: String): PlaceDomainEntity? {
        return fetchPlaceByGeoHashId(geoHash)
    }

    private suspend fun fetchPlaceByGeoHashId(geoHash: String): PlaceDomainEntity? {
        val snap = firestore
            .collection(FirestoreCollection.Content.Places.ITEMS)
            .document(geoHash)
            .get().await()
        return snap.toObject(Place::class.java)?.let {
            placeToDomainEntityMapper.map(it)
        }
    }

    override suspend fun uploadPlace(form: PlaceForm) {
        val geoHash = createGeoHash(form.latitude, form.longitude)
        val pictureRef = storage.getReference(
            FirebaseImageResizer.getPlacePictureToResizeStorageReference(geoHash)
        )

        val storageMetadata =
            StorageMetadata.Builder().setContentType(CONTENT_TYPE_IMAGE_JPEG).build()
        val uploadImageDeferred = when (val model = form.image) {
            is Bitmap -> {
                val bytes = ByteArrayOutputStream()
                model.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                pictureRef.putBytes(
                    bytes.toByteArray(),
                    storageMetadata,
                ).asDeferred()
            }

            is Uri -> {
                pictureRef.putFile(
                    model,
                    storageMetadata
                ).asDeferred()
            }

            else -> {
                throw RuntimeException("Unsupported image format")
            }
        }

        val place = placeFormToPlaceMapper.map(form)
        val placeCard = placeFormToPlaceCardMapper.map(form)
        val geoFireCompletionSource = TaskCompletionSource<Void>()
        GeoFire(database.getReference(DatabasePath.Content.Places.GEO_FIRE)).setLocation(
            /* key = */ geoHash,
            /* location = */ GeoLocation(form.latitude, form.longitude)
        )
        /* completionListener = */ { key, error ->
            if (error != null) {
                Log.e(TAG, "Error message uploading place geofire ${error.message}")
                Log.e(TAG, "Error details uploading place geofire ${error.details}")
            }
            geoFireCompletionSource.setResult(null)
        }

        val databaseRef = database
            .getReference("${DatabasePath.Content.Places.CARDS}/${geoHash}")
        val databaseDeferred = databaseRef
            .setValue(placeCard).asDeferred()
        val firestoreDeferred = firestore
            .collection(FirestoreCollection.Content.Places.ITEMS)
            .document(geoHash)
            .set(place).asDeferred()

        awaitAll(
            databaseDeferred,
            firestoreDeferred,
            uploadImageDeferred,
            geoFireCompletionSource.task.asDeferred(),
        )
    }

    private companion object {
        const val CONTENT_TYPE_IMAGE_JPEG = "image/jpeg"
    }
}