package org.codingforanimals.veganuniverse.user

import android.content.Intent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.codingforanimals.veganuniverse.user.auth.EmailAndPasswordAuthenticator
import org.codingforanimals.veganuniverse.user.auth.GmailAuthUseCase
import org.codingforanimals.veganuniverse.user.auth.LogoutUseCase
import org.codingforanimals.veganuniverse.user.model.LoginResponse
import org.codingforanimals.veganuniverse.user.model.LogoutResponse
import org.codingforanimals.veganuniverse.user.model.RegistrationResponse
import org.codingforanimals.veganuniverse.user.model.User

internal class UserRepositoryImpl(
    private val emailAndPasswordAuthenticator: EmailAndPasswordAuthenticator,
    private val gmailAuthUseCase: GmailAuthUseCase,
    private val logoutUseCase: LogoutUseCase,
) : UserRepository {

    override val gmailAuthIntent: Intent = gmailAuthUseCase.intent

    private var _user = MutableStateFlow<User>(User.GuestUser)
    override val user = _user.asStateFlow()

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String,
    ): LoginResponse {
        val response = emailAndPasswordAuthenticator.login(email, password)
        when (response) {
            is LoginResponse.Exception -> Unit
            is LoginResponse.Success -> {
                _user.emit(response.user)
            }
        }
        return response
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
    ): RegistrationResponse {
        val response = emailAndPasswordAuthenticator.register(email, password)
        when (response) {
            is RegistrationResponse.Exception -> Unit
            is RegistrationResponse.Success -> {
                _user.emit(response.user)
            }
        }
        return response
    }

    override suspend fun authenticateWithGmail(intent: Intent): RegistrationResponse {
        val response = gmailAuthUseCase(intent)
        when (response) {
            is RegistrationResponse.Exception -> Unit
            is RegistrationResponse.Success -> {
                _user.emit(response.user)
            }
        }
        return response
    }

    override suspend fun logout(): LogoutResponse {
        val response = logoutUseCase()
        when (response) {
            LogoutResponse.Exception -> Unit
            LogoutResponse.Success -> {
                _user.emit(User.GuestUser)
            }
        }
        return response
    }
}