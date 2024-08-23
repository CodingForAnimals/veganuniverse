package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.user.domain.repository.CurrentUserRepository

class EvaluateUserEmailVerificationState(
    private val currentUserRepository: CurrentUserRepository,
    private val authenticationUseCases: AuthenticationUseCases,
) {
    suspend operator fun invoke(): Result {
        return runCatching {
            val isUserVerifiedLocal = currentUserRepository.flowOnIsUserVerified().firstOrNull()
                ?: return Result.UnauthenticatedUser
            if (isUserVerifiedLocal) {
                return Result.UserIsAlreadyVerified
            }
            return if (currentUserRepository.refreshIsUserVerified()) {
                authenticationUseCases.logout()
                Result.UserHasJustVerifiedEmail
            } else {
                Result.UserHasNotYetVerifiedEmail
            }
        }.getOrElse {
            Log.e("UserEmailVerification", it.stackTraceToString())
            Analytics.logNonFatalException(it)
            authenticationUseCases.logout()
            Result.UnexpectedError
        }
    }

    sealed class Result {
        data object UnexpectedError : Result()
        data object UnauthenticatedUser : Result()
        data object UserIsAlreadyVerified : Result()
        data object UserHasJustVerifiedEmail : Result()
        data object UserHasNotYetVerifiedEmail : Result()
    }
}
