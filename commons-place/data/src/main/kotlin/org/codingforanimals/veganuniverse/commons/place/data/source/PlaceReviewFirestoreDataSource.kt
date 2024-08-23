package org.codingforanimals.veganuniverse.commons.place.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import org.codingforanimals.veganuniverse.commons.network.mapFirestoreExceptions
import org.codingforanimals.veganuniverse.commons.place.data.model.PlaceReviewFirestoreEntity
import org.codingforanimals.veganuniverse.commons.place.data.paging.PlaceReviewsPagingSource
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReviewQueryParams
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReviewSorter
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReviewUserFilter
import kotlin.math.min

internal class PlaceReviewFirestoreDataSource(
    private val placesCollection: CollectionReference,
    private val placeReviewReportsReference: DatabaseReference,
) : PlaceReviewRemoteDataSource {

    override suspend fun insertReview(placeId: String, review: PlaceReview): String {
        val newDocRef = placesCollection.document(placeId)
            .collection(REVIEWS_COLLECTION)
            .document()
        try {
            newDocRef.set(review.toNewFirestoreEntity()).await()
        } catch (e: FirebaseFirestoreException) {
            throw mapFirestoreExceptions(e)
        }
        return newDocRef.id
    }

    override suspend fun queryPlaceReviews(params: PlaceReviewQueryParams): List<PlaceReview> {
        return params.getQuery().get().await().toObjects(PlaceReviewFirestoreEntity::class.java)
            .map { it.toModel() }
    }

    override fun queryPlaceReviewsPagingFlow(params: PlaceReviewQueryParams): Flow<PagingData<PlaceReview>> {
        return Pager(
            config = PagingConfig(
                pageSize = params.pageSize,
                maxSize = params.maxSize,
            ),
            pagingSourceFactory = { PlaceReviewsPagingSource(params.getQuery()) },
        ).flow.map { pagingData -> pagingData.map { it.toModel() } }
    }

    override suspend fun deleteReview(placeId: String, reviewId: String) {
        placesCollection.document(placeId).collection(REVIEWS_COLLECTION)
            .document(reviewId).delete().await()
    }

    override suspend fun reportReview(placeId: String, reviewId: String, userId: String) {
        placeReviewReportsReference
            .child(placeId).child(reviewId)
            .child(userId).setValue(true)
            .await()
    }

    private fun PlaceReviewQueryParams.getQuery(): Query {
        var query = placesCollection.document(placeId).collection(REVIEWS_COLLECTION)
            .limit(min(maxSize, pageSize).toLong())

        when (val userFilter = userFilter) {
            is PlaceReviewUserFilter.ExcludeUser -> {
                query = query
                    .orderBy(FIELD_USER_ID)
                    .whereNotEqualTo(FIELD_USER_ID, userFilter.userId)
            }

            is PlaceReviewUserFilter.FilterByUser -> {
                query = query
                    .orderBy(FIELD_USER_ID)
                    .whereEqualTo(FIELD_USER_ID, userFilter.userId)
            }

            null -> Unit
        }

        query = query.orderBy(sorter.sortingField, sorter.sortingDirection)

        return query
    }

    private fun PlaceReviewFirestoreEntity.toModel(): PlaceReview {
        return PlaceReview(
            id = id,
            userId = userId,
            username = username,
            rating = rating ?: 0,
            title = title,
            description = description,
            createdAt = createdAt?.toDate(),
        )
    }

    private fun PlaceReview.toNewFirestoreEntity(): PlaceReviewFirestoreEntity {
        return PlaceReviewFirestoreEntity(
            id = null,
            userId = userId,
            username = username,
            rating = rating,
            title = title,
            description = description,
            createdAt = null
        )
    }

    private val PlaceReviewSorter.sortingField: String
        get() = when (this) {
            PlaceReviewSorter.DATE -> FIELD_CREATED_AT
            PlaceReviewSorter.RATING_ASCENDING -> FIELD_RATING
            PlaceReviewSorter.RATING_DESCENDING -> FIELD_RATING
        }

    private val PlaceReviewSorter.sortingDirection: Query.Direction
        get() = when (this) {
            PlaceReviewSorter.DATE -> Query.Direction.DESCENDING
            PlaceReviewSorter.RATING_ASCENDING -> Query.Direction.ASCENDING
            PlaceReviewSorter.RATING_DESCENDING -> Query.Direction.DESCENDING
        }

    companion object {
        internal const val PLACES_REVIEW_REPORTS = "content/places/review-reports"
        private const val REVIEWS_COLLECTION = "reviews"
        private const val FIELD_USER_ID = "userId"
        private const val FIELD_RATING = "rating"
        private const val FIELD_CREATED_AT = "createdAt"
    }
}