package org.codingforanimals.veganuniverse.auth

import android.content.Intent
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.auth.model.LoginResponse
import org.codingforanimals.veganuniverse.auth.model.LogoutResponse
import org.codingforanimals.veganuniverse.auth.model.RegistrationResponse
import org.codingforanimals.veganuniverse.auth.model.SendVerificationEmailResult
import org.codingforanimals.veganuniverse.auth.model.User

interface UserRepository {
    val user: Flow<User?>
    val googleSignInIntent: Intent
    suspend fun loginWithEmailAndPassword(email: String, password: String): LoginResponse
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        name: String,
    ): RegistrationResponse

    suspend fun authenticateWithGmail(intent: Intent): RegistrationResponse

    suspend fun logout(): LogoutResponse
    suspend fun sendUserVerificationEmail(): SendVerificationEmailResult
    suspend fun refreshUser(): User?
}