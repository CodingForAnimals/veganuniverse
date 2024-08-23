package org.codingforanimals.veganuniverse.place.reviews

import android.util.Log
import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class ReportPlaceReview(
    private val placeReviewRepository: PlaceReviewRepository,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    suspend operator fun invoke(
        placeId: String,
        reviewId: String,
    ): Result<Unit> = runCatching {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to report a place review"
        }
        placeReviewRepository.reportReview(
            placeId = placeId,
            reviewId = reviewId,
            userId = user.id
        )
    }.onFailure {
        Log.e("ReportPlaceReview", "Failed to report place review", it)
        Analytics.logNonFatalException(it)
    }
}