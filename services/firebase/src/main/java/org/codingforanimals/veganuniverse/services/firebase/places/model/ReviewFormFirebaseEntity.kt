package org.codingforanimals.veganuniverse.services.firebase.places.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class ReviewFormFirebaseEntity(
    val userId: String,
    val username: String,
    val rating: Int,
    val title: String,
    val description: String?,
    @ServerTimestamp val timestamp: Timestamp? = null,
)
