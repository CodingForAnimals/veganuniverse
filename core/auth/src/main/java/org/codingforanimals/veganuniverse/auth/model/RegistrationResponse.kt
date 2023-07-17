package org.codingforanimals.veganuniverse.auth.model

sealed class RegistrationResponse {
    data class Success(val user: User) : RegistrationResponse()
    sealed class Exception : RegistrationResponse() {
        object UserAlreadyExists : Exception()
        object InvalidCredentials : Exception()
        object InvalidUser : Exception()
        object UnknownFailure : Exception()
        object ConnectionError : Exception()
    }
}