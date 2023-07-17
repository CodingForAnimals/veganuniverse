package org.codingforanimals.veganuniverse.services.firebase.api.places.model

import com.google.firebase.Timestamp

data class ReviewFirebaseEntity(
    val userId: String = "",
    val username: String = "",
    val rating: Int = 0,
    val title: String = "",
    val description: String? = null,
    val timestamp: Timestamp? = null,
) {
    val timestampSeconds: Long?
        get() = timestamp?.seconds
}