package org.codingforanimals.veganuniverse.auth.model

import org.codingforanimals.veganuniverse.user.services.firebase.model.EmailLoginResponse
import org.codingforanimals.veganuniverse.user.services.firebase.model.EmailRegistrationResponse
import org.codingforanimals.veganuniverse.user.services.firebase.model.ProviderAuthenticationResponse
import org.codingforanimals.veganuniverse.user.services.firebase.model.UserFirebaseEntity
import org.codingforanimals.veganuniverse.user.services.firebase.SendVerificationEmailResult as FirebaseSendVerificationEmailResult

internal fun UserFirebaseEntity.toDomainEntity(): User {
    return User(
        id = id,
        name = name,
        email = email,
        isEmailVerified = isEmailVerified,
        profilePictureUrl = profilePictureUrl,
    )
}

internal fun EmailLoginResponse.toLoginResponse(): LoginResponse {
    return when (this) {
        EmailLoginResponse.Exception.ConnectionError -> LoginResponse.Exception.ConnectionError
        EmailLoginResponse.Exception.InvalidPassword -> LoginResponse.Exception.InvalidPassword
        EmailLoginResponse.Exception.InvalidUser -> LoginResponse.Exception.InvalidUser
        EmailLoginResponse.Exception.UnknownException -> LoginResponse.Exception.UnknownException
        EmailLoginResponse.Exception.UserNotFound -> LoginResponse.Exception.UserNotFound
        is EmailLoginResponse.Success -> LoginResponse.Success(
            userFirebaseEntity.toDomainEntity()
        )
    }
}

internal fun EmailRegistrationResponse.toRegistrationResponse(): RegistrationResponse {
    return when (this) {
        EmailRegistrationResponse.Exception.ConnectionError -> RegistrationResponse.Exception.ConnectionError
        EmailRegistrationResponse.Exception.InvalidCredentials -> RegistrationResponse.Exception.InvalidCredentials
        EmailRegistrationResponse.Exception.InvalidUser -> RegistrationResponse.Exception.InvalidUser
        EmailRegistrationResponse.Exception.UnknownFailure -> RegistrationResponse.Exception.UnknownFailure
        EmailRegistrationResponse.Exception.UserAlreadyExists -> RegistrationResponse.Exception.UserAlreadyExists
        is EmailRegistrationResponse.Success -> RegistrationResponse.Success(
            userFirebaseEntity.toDomainEntity()
        )
    }
}

internal fun ProviderAuthenticationResponse.toRegistrationResponse(): RegistrationResponse {
    return when (this) {
        ProviderAuthenticationResponse.Exception.ConnectionError -> RegistrationResponse.Exception.ConnectionError
        ProviderAuthenticationResponse.Exception.InvalidCredentials -> RegistrationResponse.Exception.InvalidCredentials
        ProviderAuthenticationResponse.Exception.InvalidUser -> RegistrationResponse.Exception.InvalidUser
        ProviderAuthenticationResponse.Exception.UnknownFailure -> RegistrationResponse.Exception.UnknownFailure
        ProviderAuthenticationResponse.Exception.UserAlreadyExists -> RegistrationResponse.Exception.UserAlreadyExists
        is ProviderAuthenticationResponse.Success -> RegistrationResponse.Success(
            userFirebaseEntity.toDomainEntity()
        )
    }
}

internal fun FirebaseSendVerificationEmailResult.toDomainResult(): SendVerificationEmailResult {
    return when (this) {
        FirebaseSendVerificationEmailResult.Success -> SendVerificationEmailResult.Success
        FirebaseSendVerificationEmailResult.TooManyRequests -> SendVerificationEmailResult.TooManyRequests
        FirebaseSendVerificationEmailResult.UnknownError -> SendVerificationEmailResult.UnknownError
    }
}