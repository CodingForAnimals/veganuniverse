package org.codingforanimals.veganuniverse.user.model

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUser(): User.LoggedUser {
    return User.LoggedUser(
        id = uid,
        name = displayName ?: email ?: "",
        email = email ?: "",
    )
}

fun FirebaseException.toRegistrationException(): RegistrationResponse.Exception {
    return when (this) {
        is FirebaseAuthUserCollisionException -> RegistrationResponse.Exception.UserAlreadyExists
        is FirebaseAuthInvalidCredentialsException -> RegistrationResponse.Exception.InvalidCredentials
        is FirebaseNetworkException -> RegistrationResponse.Exception.ConnectionError
        is FirebaseAuthInvalidUserException -> RegistrationResponse.Exception.InvalidUser
        else -> RegistrationResponse.Exception.UnknownFailure
    }
}