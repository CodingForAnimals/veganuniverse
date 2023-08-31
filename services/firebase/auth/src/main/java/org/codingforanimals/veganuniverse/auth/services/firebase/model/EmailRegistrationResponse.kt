package org.codingforanimals.veganuniverse.auth.services.firebase.model

sealed class EmailRegistrationResponse {
    data class Success(val userFirebaseEntity: UserFirebaseEntity) : EmailRegistrationResponse()
    sealed class Exception : EmailRegistrationResponse() {
        data object UserAlreadyExists : Exception()
        data object InvalidCredentials : Exception()
        data object ConnectionError : Exception()
        data object InvalidUser : Exception()
        data object UnknownFailure : Exception()
    }
}
