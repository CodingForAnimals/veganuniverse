package org.codingforanimals.veganuniverse.user.data.dto

data class UserDTO(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val isEmailVerified: Boolean = false,
)
