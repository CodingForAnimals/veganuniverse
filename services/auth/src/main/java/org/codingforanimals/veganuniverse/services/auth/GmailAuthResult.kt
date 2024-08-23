package org.codingforanimals.veganuniverse.services.auth

sealed class GmailAuthResult {
    data class NewUser(val email: String, val name: String) : GmailAuthResult()
    data object Success : GmailAuthResult()
    data class Error(val throwable: Throwable) : GmailAuthResult()
}