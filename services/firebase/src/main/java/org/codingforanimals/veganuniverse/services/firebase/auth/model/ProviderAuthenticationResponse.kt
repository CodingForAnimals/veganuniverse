package org.codingforanimals.veganuniverse.services.firebase.auth.model

sealed class ProviderAuthenticationResponse {
    data class Success(val userDto: UserDTO) : ProviderAuthenticationResponse()
    sealed class Exception : ProviderAuthenticationResponse() {
        object UserAlreadyExists : Exception()
        object InvalidCredentials : Exception()
        object ConnectionError : Exception()
        object InvalidUser : Exception()
        object UnknownFailure : Exception()
    }
}
