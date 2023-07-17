package org.codingforanimals.veganuniverse.auth.model

sealed class LoginResponse {
    data class Success(val user: User) : LoginResponse()
    sealed class Exception : LoginResponse() {
        object UserNotFound : Exception()
        object InvalidPassword : Exception()
        object InvalidUser : Exception()
        object ConnectionError : Exception()
        object UnknownException : Exception()
    }
}