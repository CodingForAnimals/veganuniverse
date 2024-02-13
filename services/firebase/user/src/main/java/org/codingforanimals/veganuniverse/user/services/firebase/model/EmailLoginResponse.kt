package org.codingforanimals.veganuniverse.user.services.firebase.model

import org.codingforanimals.veganuniverse.user.services.firebase.entity.UserFirebaseEntity

sealed class EmailLoginResponse {
    data class Success(val userFirebaseEntity: UserFirebaseEntity) : EmailLoginResponse()
    sealed class Exception : EmailLoginResponse() {
        data object UserNotFound : Exception()
        data object InvalidPassword : Exception()
        data object ConnectionError : Exception()
        data object InvalidUser : Exception()
        data object UnknownException : Exception()
    }
}