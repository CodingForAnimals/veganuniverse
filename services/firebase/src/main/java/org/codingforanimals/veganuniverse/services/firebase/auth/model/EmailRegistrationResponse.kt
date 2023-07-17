package org.codingforanimals.veganuniverse.services.firebase.auth.model

sealed class EmailRegistrationResponse {
    data class Success(val userDto: UserDTO) : EmailRegistrationResponse()
    sealed class Exception : EmailRegistrationResponse() {
        object UserAlreadyExists : Exception()
        object InvalidCredentials : Exception()
        object ConnectionError : Exception()
        object InvalidUser : Exception()
        object UnknownFailure : Exception()
    }
}
