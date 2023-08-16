package org.codingforanimals.veganuniverse.auth.services.firebase.model

sealed class EmailRegistrationResponse {
    data class Success(val userFirebaseEntity: UserFirebaseEntity) : EmailRegistrationResponse()
    sealed class Exception : EmailRegistrationResponse() {
        object UserAlreadyExists : Exception()
        object InvalidCredentials : Exception()
        object ConnectionError : Exception()
        object InvalidUser : Exception()
        object UnknownFailure : Exception()
    }
}
