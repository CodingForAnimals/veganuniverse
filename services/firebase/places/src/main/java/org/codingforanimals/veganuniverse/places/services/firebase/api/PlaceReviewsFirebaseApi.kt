package org.codingforanimals.veganuniverse.places.services.firebase.api

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.places.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.places.entity.ReviewsPaginatedResponse
import org.codingforanimals.veganuniverse.places.services.firebase.model.PaginationCursor
import org.codingforanimals.veganuniverse.places.services.firebase.model.PlaceReviewFirebaseEntity
import org.codingforanimals.veganuniverse.places.services.firebase.model.toDomainEntity
import org.codingforanimals.veganuniverse.places.services.firebase.model.toFirebaseEntity
import org.codingforanimals.veganuniverse.services.firebase.FirebaseCollection
import org.codingforanimals.veganuniverse.services.firebase.FirebaseFields

internal class PlaceReviewsFirebaseApi(
    private val firebase: FirebaseFirestore,
) : PlaceReviewsApi {

    override suspend fun fetchReview(placeId: String, userId: String): PlaceReview? {
        val snap = firebase
            .collection(FirebaseCollection.PLACES_ITEMS)
            .document(placeId)
            .collection(FirebaseCollection.PLACE_REVIEWS)
            .whereEqualTo(FirebaseFields.USER_ID, userId)
            .get().await()
        return snap
            .documents
            .firstOrNull()
            ?.toObject(PlaceReviewFirebaseEntity::class.java)
            ?.toDomainEntity()
    }

    private var cursor: PaginationCursor = PaginationCursor.FreshStart
    override suspend fun fetchReviews(placeId: String): ReviewsPaginatedResponse {
        var query = firebase
            .collection(FirebaseCollection.PLACES_ITEMS)
            .document(placeId)
            .collection(FirebaseCollection.PLACE_REVIEWS)
            .orderBy(FirebaseFields.Places.TIMESTAMP, Query.Direction.DESCENDING)

        when (val currentCursor = cursor) {
            PaginationCursor.FreshStart -> Unit
            PaginationCursor.Finished -> return ReviewsPaginatedResponse(emptyList(), false)
            is PaginationCursor.Current -> {
                query = query.startAt(currentCursor.cursor)
            }
        }

        val snap = query.limit(3).get().await()

        val reviews = snap.documents.toMutableList()
        val (hasMoreItems, updatedCursor) = if (reviews.size == 3) {
            Pair(true, PaginationCursor.Current(reviews.removeLast()))
        } else {
            Pair(false, PaginationCursor.Finished)
        }

        cursor = updatedCursor

        return ReviewsPaginatedResponse(
            reviews = reviews.mapNotNull {
                it.toObject(PlaceReviewFirebaseEntity::class.java)?.toDomainEntity()
            },
            hasMoreItems = hasMoreItems,
        )
    }

    override suspend fun submitReview(
        placeId: String,
        placeReviewForm: PlaceReviewForm,
    ): PlaceReview {
        val docId = firebase
            .collection(FirebaseCollection.PLACE_REVIEWS)
            .document(placeId)
            .collection(FirebaseCollection.PLACE_REVIEWS)
            .add(placeReviewForm.toFirebaseEntity())
            .await().id

        with(placeReviewForm) {
            return PlaceReview(
                id = docId,
                userId = userId,
                username = username,
                rating = rating,
                title = title,
                description = description,
                timestampSeconds = Timestamp.now().seconds,
            )
        }
    }

    override suspend fun deleteReview(placeId: String, reviewId: String) {
        firebase
            .collection(FirebaseCollection.PLACE_REVIEWS)
            .document(placeId)
            .collection(FirebaseCollection.PLACE_REVIEWS)
            .whereEqualTo(FirebaseFields.ID, reviewId)
            .get().await()
            .documents.first().reference
            .delete().await()
    }
}