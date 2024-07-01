package org.codingforanimals.veganuniverse.commons.place.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

internal data class PlaceReviewFirestoreEntity(
    @DocumentId val id: String? = null,
    val userId: String? = null,
    val username: String? = null,
    val rating: Int? = null,
    val title: String? = null,
    val description: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
)