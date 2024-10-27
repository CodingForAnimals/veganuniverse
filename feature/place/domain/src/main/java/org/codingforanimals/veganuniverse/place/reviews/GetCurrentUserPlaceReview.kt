package org.codingforanimals.veganuniverse.place.reviews

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.place.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.place.shared.model.PlaceReviewQueryParams
import org.codingforanimals.veganuniverse.place.shared.model.PlaceReviewUserFilter

class GetCurrentUserPlaceReview(
    private val placeReviewRepository: PlaceReviewRepository,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    suspend operator fun invoke(placeId: String): Result<PlaceReview?> {
        return runCatching {
            val userId = flowOnCurrentUser().firstOrNull()?.id ?: return@runCatching null
            val params = PlaceReviewQueryParams.Builder(placeId)
                .withPageSize(1)
                .withMaxSize(1)
                .withUserFilter(PlaceReviewUserFilter.FilterByUser(userId))
                .build()
            placeReviewRepository.queryPlaceReviews(params).firstOrNull()
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
            Analytics.logNonFatalException(it)
        }
    }

    companion object {
        private const val TAG = "GetCurrentUserReview"
    }
}