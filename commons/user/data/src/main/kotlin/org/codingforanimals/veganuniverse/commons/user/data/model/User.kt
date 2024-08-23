package org.codingforanimals.veganuniverse.commons.user.data.model

import com.google.firebase.firestore.Exclude

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    @get: Exclude val isVerified: Boolean = false,
)
