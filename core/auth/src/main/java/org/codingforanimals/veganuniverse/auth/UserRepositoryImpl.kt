package org.codingforanimals.veganuniverse.auth

import android.content.Intent
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.codingforanimals.veganuniverse.auth.model.LoginResponse
import org.codingforanimals.veganuniverse.auth.model.LogoutResponse
import org.codingforanimals.veganuniverse.auth.model.RegistrationResponse
import org.codingforanimals.veganuniverse.auth.model.User
import org.codingforanimals.veganuniverse.auth.model.toDomainEntity
import org.codingforanimals.veganuniverse.auth.model.toLoginResponse
import org.codingforanimals.veganuniverse.auth.model.toRegistrationResponse
import org.codingforanimals.veganuniverse.services.firebase.auth.EmailAuthenticator
import org.codingforanimals.veganuniverse.services.firebase.auth.GmailAuthenticator
import org.codingforanimals.veganuniverse.services.firebase.auth.LogoutUseCase
import org.codingforanimals.veganuniverse.services.firebase.auth.model.EmailLoginResponse
import org.codingforanimals.veganuniverse.services.firebase.auth.model.EmailRegistrationResponse
import org.codingforanimals.veganuniverse.services.firebase.auth.model.ProviderAuthenticationResponse

private const val TAG = "UserRepositoryImpl"

internal class UserRepositoryImpl(
    private val emailAuthenticator: EmailAuthenticator,
    private val gmailAuthenticator: GmailAuthenticator,
    private val logoutUseCase: LogoutUseCase,
) : UserRepository {

    override val gmailAuthIntent: Intent = gmailAuthenticator.intent

    private var _user = MutableStateFlow<User?>(null)
    override val user = _user.asStateFlow()

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String,
    ): LoginResponse {
        val response = emailAuthenticator.login(email, password)
        when (response) {
            is EmailLoginResponse.Exception -> Unit
            is EmailLoginResponse.Success -> {
                _user.emit(response.userDto.toDomainEntity())
            }
        }
        return response.toLoginResponse()
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
    ): RegistrationResponse {
        val response = emailAuthenticator.register(email, password)
        when (response) {
            is EmailRegistrationResponse.Exception -> Unit
            is EmailRegistrationResponse.Success -> {
                _user.emit(response.userDto.toDomainEntity())
            }
        }
        return response.toRegistrationResponse()
    }

    override suspend fun authenticateWithGmail(intent: Intent): RegistrationResponse {
        val response = gmailAuthenticator(intent)
        when (response) {
            is ProviderAuthenticationResponse.Exception -> Unit
            is ProviderAuthenticationResponse.Success -> {
                _user.emit(response.userDto.toDomainEntity())
            }
        }
        return response.toRegistrationResponse()
    }

    override suspend fun logout(): LogoutResponse {
        return try {
            logoutUseCase()
            _user.emit(null)
            LogoutResponse.Success
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            LogoutResponse.Exception
        }
    }
}