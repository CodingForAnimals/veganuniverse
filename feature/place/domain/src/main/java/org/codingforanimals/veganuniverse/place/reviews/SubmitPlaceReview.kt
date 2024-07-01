package org.codingforanimals.veganuniverse.place.reviews

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.network.PermissionDeniedException
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceReview
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class SubmitPlaceReview(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val placeReviewRepository: PlaceReviewRepository,
) {
    suspend operator fun invoke(placeId: String, placeReview: PlaceReview): Result {
        val user = flowOnCurrentUser(true).firstOrNull() ?: return Result.UnauthenticatedUser
        return try {
            if (!user.isEmailVerified) {
                return Result.UnverifiedEmail
            }
            val review = placeReview.copy(
                userId = user.id,
                username = user.name,
            )
            placeReviewRepository.insertReview(placeId, review)
            Result.Success
        } catch (e: PermissionDeniedException) {
            return if (user.isEmailVerified) {
                Result.UserMustReauthenticate
            } else {
                Result.UnverifiedEmail
            }
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            Result.UnexpectedError
        }
    }

    companion object {
        private const val TAG = "SubmitPlaceReview"
    }

    sealed class Result {
        data object Success : Result()
        data object UnauthenticatedUser : Result()
        data object UserMustReauthenticate : Result()
        data object UnverifiedEmail : Result()
        data object UnexpectedError : Result()
    }
}