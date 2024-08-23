package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.user.domain.repository.CurrentUserRepository
import org.codingforanimals.veganuniverse.services.auth.Authenticator

class ReauthenticationUseCases(
    private val context: Context,
    private val authenticator: Authenticator,
    private val currentUserRepository: CurrentUserRepository,
) {
    sealed class Result {
        data object GmailReauthenticationSuccess : Result()
        data object UserMustEnterPassword : Result()
        data object UnexpectedError : Result()
    }

    suspend fun reauthenticate(): Result {
        return runCatching {
            if (authenticator.isUserProvidedByGoogle) {
                gmailReauthentication()
                Result.GmailReauthenticationSuccess
            } else {
                Result.UserMustEnterPassword
            }
        }.getOrElse {
            Log.e(TAG, it.stackTraceToString())
            Result.UnexpectedError
        }
    }

    suspend fun emailReauthentication(password: String): kotlin.Result<Unit> {
        return runCatching {
            val email = currentUserRepository.flowOnCurrentUser().firstOrNull()?.email
                ?: throw IllegalStateException("Empty user attempting to reauthenticate")
            authenticator.emailReauthentication(email, password)
            currentUserRepository.reloadUser()
        }
    }

    private suspend fun gmailReauthentication() {
        authenticator.gmailReauthentication(context)
        currentUserRepository.reloadUser()
    }

    companion object {
        private const val TAG = "ReauthenticationUseCase"
    }
}