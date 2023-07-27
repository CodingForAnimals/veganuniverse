package org.codingforanimals.veganuniverse.services.firebase.auth

import android.content.Intent
import org.codingforanimals.veganuniverse.services.firebase.auth.model.EmailLoginResponse
import org.codingforanimals.veganuniverse.services.firebase.auth.model.EmailRegistrationResponse
import org.codingforanimals.veganuniverse.services.firebase.auth.model.ProviderAuthenticationResponse

interface Authenticator {
    suspend fun emailLogin(email: String, password: String): EmailLoginResponse
    suspend fun emailRegistration(email: String, password: String): EmailRegistrationResponse
    val googleSignInIntent: Intent
    suspend fun gmailAuthentication(intent: Intent): ProviderAuthenticationResponse
    suspend fun logout()
}