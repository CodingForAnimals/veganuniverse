package org.codingforanimals.veganuniverse.auth

import android.content.Intent
import kotlinx.coroutines.flow.StateFlow
import org.codingforanimals.veganuniverse.auth.model.LoginResponse
import org.codingforanimals.veganuniverse.auth.model.LogoutResponse
import org.codingforanimals.veganuniverse.auth.model.RegistrationResponse
import org.codingforanimals.veganuniverse.auth.model.User

interface UserRepository {
    val user: StateFlow<User?>
    val gmailAuthIntent: Intent
    suspend fun loginWithEmailAndPassword(email: String, password: String): LoginResponse
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
    ): RegistrationResponse

    suspend fun authenticateWithGmail(intent: Intent): RegistrationResponse

    suspend fun logout(): LogoutResponse
}