package org.codingforanimals.veganuniverse.place.reviews

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

private const val TAG = "ReportPlaceReview"

class ReportPlaceReview(
    private val placeReviewRepository: PlaceReviewRepository,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    suspend operator fun invoke(
        placeId: String,
        reviewId: String,
    ): Result {
        return runCatching {
            val userId = flowOnCurrentUser().firstOrNull()?.id ?: return@runCatching Result.UnauthenticatedUser
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