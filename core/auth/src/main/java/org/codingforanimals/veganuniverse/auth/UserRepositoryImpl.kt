package org.codingforanimals.veganuniverse.auth

import android.content.Intent
import android.util.Log
import kotlinx.coroutines.flow.transform
import org.codingforanimals.veganuniverse.auth.model.LoginResponse
import org.codingforanimals.veganuniverse.auth.model.LogoutResponse
import org.codingforanimals.veganuniverse.auth.model.RegistrationResponse
import org.codingforanimals.veganuniverse.auth.model.SendVerificationEmailResult
import org.codingforanimals.veganuniverse.auth.model.User
import org.codingforanimals.veganuniverse.auth.model.toDomainEntity
import org.codingforanimals.veganuniverse.auth.model.toDomainResult
import org.codingforanimals.veganuniverse.auth.model.toLoginResponse
import org.codingforanimals.veganuniverse.auth.model.toRegistrationResponse
import org.codingforanimals.veganuniverse.user.services.firebase.Authenticator

private const val TAG = "UserRepositoryImpl"

internal class UserRepositoryImpl(
    private val authenticator: Authenticator,
) : UserRepository {

    override val googleSignInIntent: Intent = authenticator.googleSignInIntent

    override val user = authenticator.userFlow
        .transform { emit(it?.toDomainEntity()) }

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String,
    ): LoginResponse {
        val response = authenticator.emailLogin(email, password)
        return response.toLoginResponse()
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        name: String,
    ): RegistrationResponse {
        val response = authenticator.emailRegistration(email, password, name)
        return response.toRegistrationResponse()
    }

    override suspend fun authenticateWithGmail(intent: Intent): RegistrationResponse {
        val response = authenticator.gmailAuthentication(intent)
        return response.toRegistrationResponse()
    }

    override suspend fun logout(): LogoutResponse {
        return try {
            authenticator.logout()
            LogoutResponse.Success
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            LogoutResponse.Exception
        }
    }

    override suspend fun sendUserVerificationEmail(): SendVerificationEmailResult {
        return authenticator.sendUserVerificationEmail().toDomainResult()
    }

    override suspend fun refreshUser(): User? {
        return authenticator.reauthenticateUser()?.toDomainEntity()
    }
}