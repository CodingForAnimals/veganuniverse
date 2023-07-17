package org.codingforanimals.veganuniverse.services.firebase.auth.model

import com.google.firebase.auth.FirebaseUser

internal fun FirebaseUser.toDto(): UserDTO {
    return UserDTO(
        id = uid,
        name = displayName ?: email ?: "",
        email = email ?: "",
    )
}