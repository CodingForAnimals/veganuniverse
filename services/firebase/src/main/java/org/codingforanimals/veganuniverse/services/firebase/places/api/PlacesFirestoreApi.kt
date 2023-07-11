package org.codingforanimals.veganuniverse.services.firebase.places.api

import android.util.Log
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.services.firebase.places.model.PlaceFirebaseEntity
import org.codingforanimals.veganuniverse.services.firebase.places.model.PlaceLocationQueryParams
import org.codingforanimals.veganuniverse.services.firebase.places.model.PlaceQueryBound
import org.codingforanimals.veganuniverse.services.firebase.places.model.ReviewFirebaseEntity
import org.codingforanimals.veganuniverse.services.firebase.places.model.dto.ReviewDTO
import org.codingforanimals.veganuniverse.services.firebase.places.model.dto.ReviewFormDTO
import org.codingforanimals.veganuniverse.services.firebase.places.model.toFirebaseEntity

class PlacesFirestoreApi(
    private val firebase: FirebaseFirestore,
) : PlacesApi {

    override suspend fun fetchPlaces(params: PlaceLocationQueryParams): List<PlaceFirebaseEntity> {
        val tasks = mutableListOf<Task<QuerySnapshot>>()
        val collection = firebase.collection(COLLECTION_PLACES)
        getBounds(params).forEach { bound ->
            val query = collection.getGeoHashQuery(bound)
            tasks.add(query.get())
        }
        val result = mutableListOf<PlaceFirebaseEntity>()
        Tasks.whenAllComplete(tasks).await()
        for (task in tasks) {
            task.result.documents.forEach { doc ->
                try {
                    val card = doc.toObject(PlaceFirebaseEntity::class.java) ?: return@forEach
                    result.add(card)
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

    private fun getBounds(params: PlaceLocationQueryParams): List<PlaceQueryBound> {
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


    override suspend fun fetchReview(placeId: String, userId: String): ReviewFirebaseEntity? {
        val snap = firebase.collection(COLLECTION_PLACES)
            .document(placeId)
            .collection(COLLECTION_REVIEWS)
            .whereEqualTo(FIELD_USER_ID, userId)
            .get().await()
        return snap.documents.firstOrNull()?.toObject(ReviewFirebaseEntity::class.java)
    }

    //    private var lastDocument: DocumentSnapshot? = null
    override suspend fun fetchReviews(id: String): List<ReviewFirebaseEntity> {
        val snap = firebase.collection(COLLECTION_PLACES)
            .document(id)
            .collection(COLLECTION_REVIEWS)
            .orderBy(FIELD_TIMESTAMP)
//            .startAt(lastDocument)
            .limit(3)
            .get()
            .await()
//        lastDocument = snap.documents.lastOrNull()
        return snap.documents.mapNotNull { it.toObject(ReviewFirebaseEntity::class.java) }
    }

    private fun Query.getGeoHashQuery(bound: PlaceQueryBound): Query {
        return this
            .orderBy(FIELD_GEO_HASH)
            .startAt(bound.startHash)
            .endAt(bound.endHash)
    }

    override suspend fun submitReview(
        placeId: String,
        reviewFormDTO: ReviewFormDTO,
    ): ReviewDTO {
        firebase.collection(COLLECTION_PLACES).document(placeId).collection(COLLECTION_REVIEWS)
            .add(reviewFormDTO.toFirebaseEntity()).await()

        return ReviewDTO(
            userId = reviewFormDTO.userId,
            username = reviewFormDTO.username,
            rating = reviewFormDTO.rating,
            title = reviewFormDTO.title,
            description = reviewFormDTO.description,
            timestampSeconds = Timestamp.now().seconds,
        )
    }

    companion object {
        private const val COLLECTION_PLACES = "/content/places/cards"
        private const val COLLECTION_REVIEWS = "reviews"
        private const val FIELD_TIMESTAMP = "timestamp"
        private const val FIELD_GEO_HASH = "geoHash"
        private const val FIELD_USER_ID = "userId"
    }
}
