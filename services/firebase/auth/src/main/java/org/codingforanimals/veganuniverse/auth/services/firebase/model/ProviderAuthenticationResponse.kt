package org.codingforanimals.veganuniverse.auth.services.firebase.model

sealed class ProviderAuthenticationResponse {
    data class Success(val userFirebaseEntity: UserFirebaseEntity) :
        ProviderAuthenticationResponse()

    sealed class Exception : ProviderAuthenticationResponse() {
        object UserAlreadyExists : Exception()
        object InvalidCredentials : Exception()
        object ConnectionError : Exception()
        object InvalidUser : Exception()
        object UnknownFailure : Exception()
    }
}
