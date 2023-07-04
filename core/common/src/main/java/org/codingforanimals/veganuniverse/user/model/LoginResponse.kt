package org.codingforanimals.veganuniverse.user.model

sealed class LoginResponse {
    data class Success(val user: User) : LoginResponse()
    sealed class Exception : LoginResponse() {
        object UserNotFound : Exception()
        object InvalidPassword : Exception()
        object ConnectionError : Exception()
        object UnknownException : Exception()
    }
}