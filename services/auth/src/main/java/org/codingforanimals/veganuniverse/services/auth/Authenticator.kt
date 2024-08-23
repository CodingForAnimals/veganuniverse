package org.codingforanimals.veganuniverse.services.auth

import android.content.Intent

interface Authenticator {
    val googleSignInIntent: Intent
    val isUserProvidedByGoogle: Boolean
    suspend fun gmailAuthentication(intent: Intent): GmailAuthResult
    suspend fun emailAuthentication(email: String, password: String)
    suspend fun emailRegistration(email: String, password: String)
    suspend fun logout()
    suspend fun isUserVerified(): Boolean
}

