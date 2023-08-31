package org.codingforanimals.veganuniverse.auth.model

sealed class LogoutResponse {
    data object Success : LogoutResponse()
    data object Exception : LogoutResponse()
}