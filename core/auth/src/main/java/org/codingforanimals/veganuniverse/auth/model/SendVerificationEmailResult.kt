package org.codingforanimals.veganuniverse.auth.model

sealed class SendVerificationEmailResult {
    data object TooManyRequests : SendVerificationEmailResult()
    data object UnknownError : SendVerificationEmailResult()
    data object Success : SendVerificationEmailResult()
}