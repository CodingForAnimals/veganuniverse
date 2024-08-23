package org.codingforanimals.veganuniverse.user.services.firebase.entity

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserFirebaseEntity(
    val userId: String,
    val name: String,
    val email: String,
    @get:Exclude val isEmailVerified: Boolean,
    val profilePictureUrl: String? = null,
)