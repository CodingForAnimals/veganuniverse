package org.codingforanimals.veganuniverse.services.firebase.api

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.places.entity.PaginatedResponse
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.services.firebase.FirestoreCollection
import org.codingforanimals.veganuniverse.services.firebase.FirestoreFields
import org.codingforanimals.veganuniverse.services.firebase.entity.PlaceReview
import org.codingforanimals.veganuniverse.services.firebase.model.PaginationCursor
import org.codingforanimals.veganuniverse.places.entity.PlaceReview as PlaceReviewDomainEntity

private const val TAG = "PlaceReviewsFirebaseApi"

internal class PlaceReviewsFirebaseApi(
    private val firestore: FirebaseFirestore,
    private val placeReviewToDomainEntityMapper: OneWayEntityMapper<PlaceReview, PlaceReviewDomainEntity>,
    private val placeReviewFormToFirebaseEntityMapper: OneWayEntityMapper<PlaceReviewForm, PlaceReview>,
) : PlaceReviewsApi {

    override suspend fun fetchReview(
        placeId: String,
        userId: String,
    ): PlaceReviewDomainEntity? {
        val snap = firestore
            .collection(FirestoreCollection.Content.Places.reviews(placeId))
            .whereEqualTo(FirestoreFields.USER_ID, userId)
            .get().await()
        return snap
            .documents
            .firstOrNull()
            ?.toObject(PlaceReview::class.java)
            ?.let { placeReviewToDomainEntityMapper.map(it) }
    }

    private var cursor: PaginationCursor = PaginationCursor.FreshStart
    override suspend fun fetchReviews(placeId: String): PaginatedResponse<PlaceReviewDomainEntity> {
        var query = firestore
            .collection(FirestoreCollection.Content.Places.reviews(placeId))
            .orderBy(FirestoreFields.Places.TIMESTAMP, Query.Direction.DESCENDING)

        when (val currentCursor = cursor) {
            PaginationCursor.FreshStart -> Unit
            PaginationCursor.Finished -> return PaginatedResponse(emptyList(), false)
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

        return PaginatedResponse(
            content = reviews.mapNotNull { reviewSnapshot ->
                reviewSnapshot.toObject(PlaceReview::class.java)?.let { placeReview ->
                    placeReviewToDomainEntityMapper.map(placeReview)
                }
            },
            hasMoreItems = hasMoreItems,
        )
    }

    override suspend fun submitReview(
        placeId: String,
        placeReviewForm: PlaceReviewForm,
    ): PlaceReviewDomainEntity {
        val placeReview = placeReviewFormToFirebaseEntityMapper.map(placeReviewForm)
        val docRef = firestore
            .collection(FirestoreCollection.Content.Places.reviews(placeId))
            .add(placeReview).await()

        return with(placeReviewForm) {
            PlaceReviewDomainEntity(
                id = docRef.id,
                userId = userId,
                username = username,
                rating = rating,
                title = title,
                description = description,
                timestampSeconds = Timestamp.now().seconds,
            )
        }
    }

    override suspend fun deleteReview(placeId: String, placeReview: PlaceReviewDomainEntity) {
        firestore
            .collection(FirestoreCollection.Content.Places.reviews(placeId))
            .document(placeReview.id).delete().await()
    }
}