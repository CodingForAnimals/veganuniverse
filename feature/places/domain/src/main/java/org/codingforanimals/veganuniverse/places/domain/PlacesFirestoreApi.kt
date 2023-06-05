package org.codingforanimals.veganuniverse.places.domain

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class PlacesFirestoreApi : PlacesApi {

    private val firebase = FirebaseFirestore.getInstance()

    override suspend fun fetchPlaces(bounds: List<PlaceQueryBound>): List<PlaceDomainEntity> {
        val tasks = mutableListOf<Task<QuerySnapshot>>()
        val collection = firebase.collection(PLACES_COLLECTION)
        bounds.forEach { bound ->
            val query = collection.getGeoHashQuery(bound)
            tasks.add(query.get())
        }
        val result = mutableListOf<PlaceDomainEntity>()
        Tasks.whenAllComplete(tasks).await()
        for (task in tasks) {
            task.result.documents.forEach { doc ->
                try {
                    val card = doc.toObject(PlaceFirebaseEntity::class.java) ?: return@forEach
                    result.add(card.toDomainEntity())
                } catch (e: Throwable) {
                    Log.e(
                        "PlacesFirestoreApi.kt",
                        "Unable to parse object. Msg: ${e.stackTraceToString()}"
                    )
                }
            }
        }
        return result
    }

    private fun Query.getGeoHashQuery(bound: PlaceQueryBound): Query {
        return this
            .orderBy(FIELD_GEO_HASH)
            .startAt(bound.startHash)
            .endAt(bound.endHash)
    }

    companion object {
        private const val PLACES_COLLECTION = "/content/places/cards"
        private const val FIELD_TYPE = "type"
        private const val FIELD_GEO_HASH = "geoHash"
    }
}

private data class PlaceFirebaseEntity(
    val id: String = "",
    val imageRef: String = "",
    val type: String = "",
    val name: String = "",
    val rating: Int = -1,
    val reviewsCount: Int = 0,
    val address: String = "",
    val city: String = "",
    val tags: List<String> = emptyList(),
    val timestamp: Timestamp? = null,
)

private fun PlaceFirebaseEntity.toDomainEntity() =
    PlaceDomainEntity(
        id = id,
        imageRef = imageRef,
        type = type,
        name = name,
        rating = rating,
        reviewCount = reviewsCount,
        address = address,
        city = city,
        tags = tags,
        timestamp = timestamp?.seconds ?: -1
    )