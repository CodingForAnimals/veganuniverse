package org.codingforanimals.veganuniverse.places.services.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

internal data class PlaceReview(
    @DocumentId val id: String = "",
    val userId: String = "",
    val username: String = "",
    val rating: Int = 0,
    val title: String = "",
    val description: String? = null,
    @ServerTimestamp val timestamp: Timestamp? = null,
)