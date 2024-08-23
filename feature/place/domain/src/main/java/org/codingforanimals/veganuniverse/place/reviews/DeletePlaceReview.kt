package org.codingforanimals.veganuniverse.place.reviews

import android.util.Log
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfilePlaceReviewUseCases

private const val TAG = "DeletePlaceReview"

class DeletePlaceReview(
    private val placeReviewRepository: PlaceReviewRepository,
    private val profilePlaceReviewUseCases: ProfilePlaceReviewUseCases,
) {
    suspend operator fun invoke(placeId: String, reviewId: String): Result<Unit> = runCatching {
        placeReviewRepository.deleteReview(placeId, reviewId)
        profilePlaceReviewUseCases.removePlaceReview(placeId)
    }.onFailure {
        Log.e(TAG, it.stackTraceToString())
        Analytics.logNonFatalException(it)
    }
}
