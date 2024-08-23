package org.codingforanimals.veganuniverse.place.reviews

import android.util.Log
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.place.model.PlaceReview
import org.codingforanimals.veganuniverse.place.model.PlaceReviewQueryParams
import org.codingforanimals.veganuniverse.place.model.PlaceReviewSorter
import org.codingforanimals.veganuniverse.place.model.PlaceReviewUserFilter
import org.codingforanimals.veganuniverse.user.domain.usecase.GetCurrentUser

class GetLatestPlaceReviewsPagingFlow(
    private val placeReviewRepository: PlaceReviewRepository,
    private val getCurrentUser: GetCurrentUser,
) {
    operator fun invoke(placeId: String): Flow<PagingData<PlaceReview>> = flow {
        var params = PlaceReviewQueryParams.Builder(placeId)
            .withPageSize(10)
            .withSorter(PlaceReviewSorter.DATE)

        val userId = getCurrentUser()?.id
        userId?.let { params = params.withUserFilter(PlaceReviewUserFilter.ExcludeUser(it)) }

        emitAll(placeReviewRepository.queryPlaceReviewsPagingFlow(params.build()))
    }
}


class GetCurrentUserPlaceReview(
    private val placeReviewRepository: PlaceReviewRepository,
    private val getCurrentUser: GetCurrentUser,
) {
    suspend operator fun invoke(placeId: String): PlaceReview? {
        return runCatching {
            val userId = getCurrentUser()?.id ?: return null
            val params = PlaceReviewQueryParams.Builder(placeId)
                .withPageSize(1)
                .withMaxSize(1)
                .withUserFilter(PlaceReviewUserFilter.FilterByUser(userId))
                .build()
            placeReviewRepository.queryPlaceReviews(params).firstOrNull()
        }.onFailure { Log.e(TAG, it.stackTraceToString()) }
            .getOrNull()
    }

    companion object {
        private const val TAG = "GetCurrentUserReview"
    }
}