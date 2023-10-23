package org.codingforanimals.veganuniverse.user.services.firebase.model

sealed class SendVerificationEmailResult {
    data object TooManyRequests : SendVerificationEmailResult()
    data object UnknownError : SendVerificationEmailResult()
    data object Success : SendVerificationEmailResult()
}