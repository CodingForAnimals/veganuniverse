package org.codingforanimals.veganuniverse.places.services.firebase.api

import android.util.Log
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.places.entity.GeoLocationQueryParams
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.PlaceForm
import org.codingforanimals.veganuniverse.places.services.firebase.model.PlaceFirebaseEntity
import org.codingforanimals.veganuniverse.places.services.firebase.model.PlaceQueryBound
import org.codingforanimals.veganuniverse.places.services.firebase.model.toDomainEntity
import org.codingforanimals.veganuniverse.places.services.firebase.model.toFirebaseEntity
import org.codingforanimals.veganuniverse.places.services.firebase.utils.createGeoHash
import org.codingforanimals.veganuniverse.services.firebase.FirebaseCollection
import org.codingforanimals.veganuniverse.services.firebase.FirebaseFields

private const val TAG = "PlacesFirebaseApi"

internal class PlacesFirebaseApi(
    private val firebase: FirebaseFirestore,
) : PlacesApi {

    override suspend fun fetchPlace(
        latitude: Double,
        longitude: Double,
    ): Place? {
        val snap = firebase
            .collection(FirebaseCollection.PLACES_ITEMS)
            .whereEqualTo(FirebaseFields.Places.GEO_HASH, createGeoHash(latitude, longitude))
            .get().await()
        return snap
            .documents
            .firstOrNull()
            ?.toObject(PlaceFirebaseEntity::class.java)
            ?.toDomainEntity()
    }

    override suspend fun fetchPlaces(params: GeoLocationQueryParams): List<Place> {
        val tasks = mutableListOf<Task<QuerySnapshot>>()
        val placesCollection = firebase.collection(FirebaseCollection.PLACES_ITEMS)
        for (bound in getBounds(params)) {
            val boundQuery = placesCollection
                .getGeoHashQuery(bound)
                .whereEqualTo(FirebaseFields.Places.VERIFIED, true)
            tasks.add(boundQuery.get())
        }

        val result = mutableListOf<Place>()
        val taskResults = Tasks.whenAllSuccess<QuerySnapshot>(tasks).await()
        for (taskResult in taskResults) {
            taskResult.documents.forEach { doc ->
                try {
                    val item = doc.toObject(PlaceFirebaseEntity::class.java)?.toDomainEntity()
                        ?: return@forEach
                    result.add(item)
                } catch (e: Throwable) {
                    Log.e(TAG, e.stackTraceToString())
                }
            }
        }
        return result
    }

    private fun Query.getGeoHashQuery(bound: PlaceQueryBound): Query {
        return orderBy(FirebaseFields.Places.GEO_HASH)
            .startAt(bound.startHash)
            .endAt(bound.endHash)
    }

    private fun getBounds(params: GeoLocationQueryParams): List<PlaceQueryBound> {
        val geoLocation = GeoLocation(params.latitude, params.longitude)
        return GeoFireUtils
            .getGeoHashQueryBounds(geoLocation, params.radiusInMeters)
            .map {
                PlaceQueryBound(
                    it.startHash,
                    it.endHash
                )
            }
    }

    override suspend fun uploadPlace(form: PlaceForm) {
        firebase.collection(FirebaseCollection.PLACES_ITEMS).add(form.toFirebaseEntity()).await()
    }
}