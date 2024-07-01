package org.codingforanimals.veganuniverse.place.reviews

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

private const val TAG = "DeletePlaceReview"

class DeletePlaceReview(
    private val placeReviewRepository: PlaceReviewRepository,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    suspend operator fun invoke(placeId: String, reviewId: String): Result {
        return runCatching {
            flowOnCurrentUser().firstOrNull() ?: return Result.UnexpectedError
            placeReviewRepository.deleteReview(placeId, reviewId)
            Result.Success
        }.getOrElse {
            Log.e(TAG, it.stackTraceToString())
            Result.UnexpectedError
        }
    }

    sealed class Result {
        data object Success : Result()
        data object UnexpectedError : Result()
    }
}