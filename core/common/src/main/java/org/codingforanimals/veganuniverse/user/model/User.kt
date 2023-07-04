package org.codingforanimals.veganuniverse.user.model

sealed class User {
    data class LoggedUser(
        val id: String,
        val name: String,
        val email: String,
    ) : User()

    object GuestUser : User()
}