package org.codingforanimals.veganuniverse.place.reviews

import android.util.Log
import org.codingforanimals.veganuniverse.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.place.model.PlaceReview

class SubmitPlaceReview(
    private val placeReviewRepository: PlaceReviewRepository,
) {
    suspend operator fun invoke(placeId: String, placeReview: PlaceReview): Result {
        return runCatching {
            placeReviewRepository.insertReview(placeId, placeReview)
            Result.Success
        }.getOrElse {
            Log.e(TAG, it.stackTraceToString())
            Result.UnexpectedError
        }
    }

    companion object {
        private const val TAG = "SubmitPlaceReview"
    }

    sealed class Result {
        data object Success : Result()
        data object UnexpectedError : Result()
    }
}