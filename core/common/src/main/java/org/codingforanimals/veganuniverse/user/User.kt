package org.codingforanimals.veganuniverse.user

abstract class User

data class LoggedUser(
    val id: String,
    val name: String,
    val email: String,
): User() {
    companion object {
        fun aLoggedUser() = LoggedUser("123", "name", "email@email.com")
    }
}

object GuestUser: User()

class IllegalUserState(state: User?): Exception("Illegal user state: $state")