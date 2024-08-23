package org.codingforanimals.veganuniverse.commons.place.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReviewQueryParams

interface PlaceReviewRepository {
    suspend fun insertReview(placeId: String, review: PlaceReview): String
    suspend fun queryPlaceReviews(params: PlaceReviewQueryParams): List<PlaceReview>
    fun queryPlaceReviewsPagingFlow(params: PlaceReviewQueryParams): Flow<PagingData<PlaceReview>>
    suspend fun reportReview(placeId: String, reviewId: String, userId: String)
    suspend fun deleteReview(placeId: String, reviewId: String)
}

