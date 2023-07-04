package org.codingforanimals.veganuniverse.user.auth

import org.codingforanimals.veganuniverse.user.model.LoginResponse
import org.codingforanimals.veganuniverse.user.model.RegistrationResponse

internal interface EmailAndPasswordAuthenticator {
    suspend fun login(email: String, password: String): LoginResponse
    suspend fun register(email: String, password: String): RegistrationResponse
}