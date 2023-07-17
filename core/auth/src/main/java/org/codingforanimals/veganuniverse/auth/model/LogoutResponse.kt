package org.codingforanimals.veganuniverse.auth.model

sealed class LogoutResponse {
    object Success : LogoutResponse()
    object Exception : LogoutResponse()
}