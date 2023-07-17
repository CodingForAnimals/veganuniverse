package org.codingforanimals.veganuniverse.services.firebase.auth

import org.codingforanimals.veganuniverse.services.firebase.auth.model.EmailLoginResponse
import org.codingforanimals.veganuniverse.services.firebase.auth.model.EmailRegistrationResponse

interface EmailAuthenticator {
    suspend fun login(email: String, password: String): EmailLoginResponse
    suspend fun register(email: String, password: String): EmailRegistrationResponse
}