package org.codingforanimals.veganuniverse.place.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.place.data.source.PlaceReviewRemoteDataSource
import org.codingforanimals.veganuniverse.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.place.shared.model.PlaceReviewQueryParams

internal class PlaceReviewRepositoryImpl(
    private val dataSource: PlaceReviewRemoteDataSource,
) : PlaceReviewRepository {
    override suspend fun insertReview(placeId: String, review: PlaceReview): String {
        return dataSource.insertReview(placeId, review)
    }

    override suspend fun queryPlaceReviews(params: PlaceReviewQueryParams): List<PlaceReview> {
        return dataSource.queryPlaceReviews(params)
    }

    override fun queryPlaceReviewsPagingFlow(params: PlaceReviewQueryParams): Flow<PagingData<PlaceReview>> {
        return dataSource.queryPlaceReviewsPagingFlow(params)
    }

    override suspend fun reportReview(placeId: String, reviewId: String, userId: String) {
        dataSource.reportReview(placeId, reviewId, userId)
    }

    override suspend fun deleteReview(placeId: String, reviewId: String) {
        dataSource.deleteReview(placeId, reviewId)
    }

}