package org.codingforanimals.veganuniverse.place.reviews

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfilePlaceReviewUseCases
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class SubmitPlaceReview(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val placeReviewRepository: PlaceReviewRepository,
    private val profilePlaceReviewUseCases: ProfilePlaceReviewUseCases,
) {
    suspend operator fun invoke(placeId: String, placeReview: PlaceReview): Result<String> =
        runCatching {
            val user = checkNotNull(flowOnCurrentUser().firstOrNull()) {
                "User must be logged in to submit a review"
            }
            val review = placeReview.copy(
                userId = user.id,
                username = user.name,
            )
            placeReviewRepository.insertReview(placeId, review).also {
                profilePlaceReviewUseCases.addPlaceReview(placeId)
            }
        }.onFailure {
            Log.e("SubmitReview", "Error submitting place review", it)
            Analytics.logNonFatalException(it)
        }
}
