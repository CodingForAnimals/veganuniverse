package org.codingforanimals.veganuniverse.place.reviews

import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class SubmitPlaceReview(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val placeReviewRepository: PlaceReviewRepository,
) {
    suspend operator fun invoke(placeId: String, placeReview: PlaceReview): Result<Unit> =
        runCatching {
            val user = checkNotNull(flowOnCurrentUser().firstOrNull()) {
                "User must be logged in to submit a review"
            }
            val review = placeReview.copy(
                userId = user.id,
                username = user.name,
            )
            placeReviewRepository.insertReview(placeId, review)
        }
}
