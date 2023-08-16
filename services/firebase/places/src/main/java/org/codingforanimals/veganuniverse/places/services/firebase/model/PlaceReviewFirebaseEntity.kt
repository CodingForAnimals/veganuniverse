package org.codingforanimals.veganuniverse.places.services.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class PlaceReviewFirebaseEntity(
    @DocumentId val id: String = "",
    val userId: String = "",
    val username: String = "",
    val rating: Int = 0,
    val title: String = "",
    val description: String? = null,
    val timestamp: Timestamp? = null,
) {
    val timestampSeconds: Long
        get() = timestamp?.seconds ?: -1
}