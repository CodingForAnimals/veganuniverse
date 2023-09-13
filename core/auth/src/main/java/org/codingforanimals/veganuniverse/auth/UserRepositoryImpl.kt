package org.codingforanimals.veganuniverse.auth

import android.content.Intent
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
import org.codingforanimals.veganuniverse.user.services.firebase.model.EmailLoginResponse
import org.codingforanimals.veganuniverse.user.services.firebase.model.EmailRegistrationResponse
import org.codingforanimals.veganuniverse.user.services.firebase.model.ProviderAuthenticationResponse

private const val TAG = "UserRepositoryImpl"

internal class UserRepositoryImpl(
    private val authenticator: Authenticator,
) : UserRepository {

    override val googleSignInIntent: Intent = authenticator.googleSignInIntent

    private var _user = MutableStateFlow<User?>(null)
    override val user = _user.asStateFlow()

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String,
    ): LoginResponse {
        val response = authenticator.emailLogin(email, password)
        when (response) {
            is EmailLoginResponse.Exception -> Unit
            is EmailLoginResponse.Success -> {
                _user.emit(response.userFirebaseEntity.toDomainEntity())
            }
        }
        return response.toLoginResponse()
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        name: String,
    ): RegistrationResponse {
        val response = authenticator.emailRegistration(email, password, name)
        when (response) {
            is EmailRegistrationResponse.Exception -> Unit
            is EmailRegistrationResponse.Success -> {
                _user.emit(response.userFirebaseEntity.toDomainEntity())
            }
        }
        return response.toRegistrationResponse()
    }

    override suspend fun authenticateWithGmail(intent: Intent): RegistrationResponse {
        val response = authenticator.gmailAuthentication(intent)
        when (response) {
            is ProviderAuthenticationResponse.Exception -> Unit
            is ProviderAuthenticationResponse.Success -> {
                _user.emit(response.userFirebaseEntity.toDomainEntity())
            }
        }
        return response.toRegistrationResponse()
    }

    override suspend fun logout(): LogoutResponse {
        return try {
            authenticator.logout()
            _user.emit(null)
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