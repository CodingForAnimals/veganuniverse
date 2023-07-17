package org.codingforanimals.veganuniverse.registration.presentation.model

import org.codingforanimals.veganuniverse.auth.model.LoginResponse
import org.codingforanimals.veganuniverse.auth.model.RegistrationResponse

fun RegistrationResponse.Exception.toUserAuthException(): RegistrationStatus.Exception {
    return when (this) {
        RegistrationResponse.Exception.ConnectionError -> RegistrationStatus.Exception.ConnectionError
        RegistrationResponse.Exception.InvalidCredentials -> RegistrationStatus.Exception.InvalidCredentials
        RegistrationResponse.Exception.InvalidUser -> RegistrationStatus.Exception.InvalidUser
        RegistrationResponse.Exception.UnknownFailure -> RegistrationStatus.Exception.UnknownFailure
        RegistrationResponse.Exception.UserAlreadyExists -> RegistrationStatus.Exception.UserAlreadyExists
    }
}

fun LoginResponse.Exception.toEmailSignInException(): EmailSignInStatus.Exception {
    return when (this) {
        LoginResponse.Exception.ConnectionError -> EmailSignInStatus.Exception.ConnectionError
        LoginResponse.Exception.InvalidPassword -> EmailSignInStatus.Exception.InvalidPassword
        LoginResponse.Exception.UnknownException -> EmailSignInStatus.Exception.UnknownException
        LoginResponse.Exception.InvalidUser -> EmailSignInStatus.Exception.InvalidUser
        LoginResponse.Exception.UserNotFound -> EmailSignInStatus.Exception.UserNotFound
    }
}
