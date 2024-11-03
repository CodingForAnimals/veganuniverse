package org.codingforanimals.veganuniverse.commons.user.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
) {
    val isValidator: Boolean
        get() = UserRole.VALIDATOR == role
}
