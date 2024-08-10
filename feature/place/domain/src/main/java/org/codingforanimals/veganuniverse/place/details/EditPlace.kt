package org.codingforanimals.veganuniverse.place.details

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class EditPlace(
    private val placeRepository: PlaceRepository,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    suspend operator fun invoke(placeId: String, edition: String): Result {
        return runCatching {
            val user = flowOnCurrentUser(true).firstOrNull()
                ?: return@runCatching Result.UnauthenticatedUser

            if (!user.isVerified) {
                return@runCatching Result.UnverifiedEmail
            }

            placeRepository.editPlace(placeId, user.id, edition)
            Result.Success
        }.getOrElse {
            Log.e(TAG, it.stackTraceToString())
            Result.UnexpectedError
        }
    }

    sealed class Result {
        data object UnauthenticatedUser : Result()
        data object UnexpectedError : Result()
        data object UnverifiedEmail : Result()
        data object Success : Result()
    }

    companion object {
        private const val TAG = "ReportPlace"
    }
}