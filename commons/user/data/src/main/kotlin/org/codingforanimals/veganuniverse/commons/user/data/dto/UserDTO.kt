package org.codingforanimals.veganuniverse.commons.user.data.dto

data class UserDTO(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val isEmailVerified: Boolean = false,
)
