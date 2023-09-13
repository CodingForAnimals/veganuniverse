package org.codingforanimals.veganuniverse.user.services.firebase.model

sealed class ProviderAuthenticationResponse {
    data class Success(val userFirebaseEntity: UserFirebaseEntity) :
        ProviderAuthenticationResponse()

    sealed class Exception : ProviderAuthenticationResponse() {
        data object UserAlreadyExists : Exception()
        data object InvalidCredentials : Exception()
        data object ConnectionError : Exception()
        data object InvalidUser : Exception()
        data object UnknownFailure : Exception()
    }
}
