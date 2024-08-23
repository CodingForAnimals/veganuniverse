package org.codingforanimals.veganuniverse.commons.user.domain.usecase

import android.content.Intent
import android.util.Log
import org.codingforanimals.veganuniverse.commons.user.domain.repository.CurrentUserRepository
import org.codingforanimals.veganuniverse.services.auth.Authenticator
import org.codingforanimals.veganuniverse.services.auth.GmailAuthResult

class AuthenticationUseCases(
    private val authenticator: Authenticator,
    private val currentUserRepository: CurrentUserRepository,
) {

    val googleSignInIntent: Intent = authenticator.googleSignInIntent

    suspend fun loginWithEmailAndPassword(
        email: String,
        password: String,
    ): Result<Unit> {
        return runCatching {
            authenticator.emailAuthentication(email, password)
            currentUserRepository.reloadUser()
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
        }
    }

    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        name: String,
    ): Result<Unit> {
        return runCatching {
            authenticator.emailRegistration(email, password)
            currentUserRepository.createUser(email, name)
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
        }
    }

    suspend fun authenticateWithGmail(intent: Intent): Result<Unit> {
        return runCatching {
            when (val response = authenticator.gmailAuthentication(intent)) {
                GmailAuthResult.Success -> {
                    currentUserRepository.reloadUser()
                }
                is GmailAuthResult.NewUser -> {
                    currentUserRepository.createUser(response.email, response.name)
                }

                is GmailAuthResult.Error -> throw Exception(response.throwable)
            }
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
        }
    }

    suspend fun logout(): Result<Unit> {
        return runCatching {
            authenticator.logout()
            currentUserRepository.clearUser()
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
        }
    }

    companion object {
        private const val TAG = "AuthenticationUseCases"
    }
}
