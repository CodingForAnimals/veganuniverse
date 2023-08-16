package org.codingforanimals.veganuniverse.auth.services.firebase.model

sealed class EmailLoginResponse {
    data class Success(val userFirebaseEntity: UserFirebaseEntity) : EmailLoginResponse()
    sealed class Exception : EmailLoginResponse() {
        object UserNotFound : Exception()
        object InvalidPassword : Exception()
        object ConnectionError : Exception()
        object InvalidUser : Exception()
        object UnknownException : Exception()
    }
}