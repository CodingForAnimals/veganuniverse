package org.codingforanimals.veganuniverse.auth.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val isEmailVerified: Boolean,
    val profilePictureUrl: String?,
)
