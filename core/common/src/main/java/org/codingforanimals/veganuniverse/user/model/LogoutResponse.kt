package org.codingforanimals.veganuniverse.user.model

sealed class LogoutResponse {
    object Success : LogoutResponse()
    object Exception : LogoutResponse()
}