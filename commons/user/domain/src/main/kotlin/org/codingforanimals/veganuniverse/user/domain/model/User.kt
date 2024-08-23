package org.codingforanimals.veganuniverse.user.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val isEmailVerified: Boolean,
)
