package org.codingforanimals.veganuniverse.auth.services.firebase.model

import com.google.firebase.auth.FirebaseUser

internal fun FirebaseUser.toFirebaseEntity(): UserFirebaseEntity {
    return UserFirebaseEntity(
        id = uid,
        name = displayName ?: email ?: "",
        email = email ?: "",
        isEmailVerified = isEmailVerified,
    )
}