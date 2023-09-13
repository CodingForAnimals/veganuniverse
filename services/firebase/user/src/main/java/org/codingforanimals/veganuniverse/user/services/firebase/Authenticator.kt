package org.codingforanimals.veganuniverse.user.services.firebase

import android.content.Intent
import org.codingforanimals.veganuniverse.user.services.firebase.model.EmailLoginResponse
import org.codingforanimals.veganuniverse.user.services.firebase.model.EmailRegistrationResponse
import org.codingforanimals.veganuniverse.user.services.firebase.model.ProviderAuthenticationResponse
import org.codingforanimals.veganuniverse.user.services.firebase.model.UserFirebaseEntity

interface Authenticator {
    suspend fun emailLogin(email: String, password: String): EmailLoginResponse
    suspend fun reauthenticateUser(): UserFirebaseEntity?
    suspend fun emailRegistration(
        email: String,
        password: String,
        name: String,
    ): EmailRegistrationResponse

    val googleSignInIntent: Intent
    suspend fun gmailAuthentication(intent: Intent): ProviderAuthenticationResponse
    suspend fun sendUserVerificationEmail(): SendVerificationEmailResult
    suspend fun logout()
}