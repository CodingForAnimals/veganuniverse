package org.codingforanimals.veganuniverse.services.firebase.auth.model

sealed class EmailLoginResponse {
    data class Success(val userDto: UserDTO) : EmailLoginResponse()
    sealed class Exception : EmailLoginResponse() {
        object UserNotFound : Exception()
        object InvalidPassword : Exception()
        object ConnectionError : Exception()
        object InvalidUser : Exception()
        object UnknownException : Exception()
    }
}