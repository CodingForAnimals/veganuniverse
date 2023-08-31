package org.codingforanimals.veganuniverse.auth.model

sealed class LoginResponse {
    data class Success(val user: User) : LoginResponse()
    sealed class Exception : LoginResponse() {
        data object UserNotFound : Exception()
        data object InvalidPassword : Exception()
        data object InvalidUser : Exception()
        data object ConnectionError : Exception()
        data object UnknownException : Exception()
    }
}