package org.codingforanimals.veganuniverse.auth.services.firebase

import android.content.Intent
import org.codingforanimals.veganuniverse.auth.services.firebase.model.EmailLoginResponse
import org.codingforanimals.veganuniverse.auth.services.firebase.model.EmailRegistrationResponse
import org.codingforanimals.veganuniverse.auth.services.firebase.model.ProviderAuthenticationResponse

interface Authenticator {
    suspend fun emailLogin(email: String, password: String): EmailLoginResponse
    suspend fun emailRegistration(email: String, password: String): EmailRegistrationResponse
    val googleSignInIntent: Intent
    suspend fun gmailAuthentication(intent: Intent): ProviderAuthenticationResponse
    suspend fun logout()
}