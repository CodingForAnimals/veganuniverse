package org.codingforanimals.veganuniverse.auth.model

sealed class RegistrationResponse {
    data class Success(val user: User) : RegistrationResponse()
    sealed class Exception : RegistrationResponse() {
        data object UserAlreadyExists : Exception()
        data object InvalidCredentials : Exception()
        data object InvalidUser : Exception()
        data object UnknownFailure : Exception()
        data object ConnectionError : Exception()
    }
}