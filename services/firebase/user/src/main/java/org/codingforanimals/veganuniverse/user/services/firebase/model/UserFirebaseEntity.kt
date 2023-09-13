package org.codingforanimals.veganuniverse.user.services.firebase.model

data class UserFirebaseEntity(
    val id: String,
    val name: String,
    val email: String,
    val isEmailVerified: Boolean,
    val profilePictureUrl: String,
)