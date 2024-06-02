package org.codingforanimals.veganuniverse.place.reviews

import android.util.Log
import org.codingforanimals.veganuniverse.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.user.domain.usecase.GetCurrentUser

private const val TAG = "DeletePlaceReview"

class DeletePlaceReview(
    private val placeReviewRepository: PlaceReviewRepository,
    private val getCurrentUser: GetCurrentUser,
) {
    suspend operator fun invoke(placeId: String, reviewId: String): Result {
        return runCatching {
            getCurrentUser() ?: return Result.UnexpectedError
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