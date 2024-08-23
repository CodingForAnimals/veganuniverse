package org.codingforanimals.veganuniverse.place.reviews

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReviewQueryParams
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReviewUserFilter
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

private const val TAG = "GetPlaceReviews"

class GetPlaceReviews(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val placeReviewRepository: PlaceReviewRepository,
) {
    suspend operator fun invoke(placeId: String): Result<List<PlaceReview>> {
        val userId = runCatching { flowOnCurrentUser().firstOrNull() }
            .onFailure { Log.e(TAG, it.stackTraceToString()) }.getOrNull()?.id

        val placeReviewsParams = if (userId != null) {
            PlaceReviewQueryParams.Builder(placeId)
                .withMaxSize(2)
                .withPageSize(2)
                .withUserFilter(PlaceReviewUserFilter.ExcludeUser(userId))
        } else {
            PlaceReviewQueryParams.Builder(placeId)
                .withMaxSize(3)
                .withPageSize(3)
        }.build()

        return runCatching {
            placeReviewRepository.queryPlaceReviews(placeReviewsParams)
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
            Analytics.logNonFatalException(it)
        }
    }
}
