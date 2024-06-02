package org.codingforanimals.veganuniverse.place.reviews

import android.util.Log
import org.codingforanimals.veganuniverse.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.user.domain.usecase.GetCurrentUser

private const val TAG = "ReportPlaceReview"

class ReportPlaceReview(
    private val placeReviewRepository: PlaceReviewRepository,
    private val getCurrentUser: GetCurrentUser,
) {
    suspend operator fun invoke(
        placeId: String,
        reviewId: String,
    ): Result {
        return runCatching {
            val userId = getCurrentUser()?.id ?: return@runCatching Result.UnauthenticatedUser
            placeReviewRepository.reportReview(
                placeId = placeId,
                reviewId = reviewId,
                userId = userId
            )
            Result.Success
        }.getOrElse {
            Log.e(TAG, it.stackTraceToString())
            Result.UnexpectedError
        }
    }

    sealed class Result {
        data object UnexpectedError : Result()
        data object UnauthenticatedUser : Result()
        data object Success : Result()
    }
}