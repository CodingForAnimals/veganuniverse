package org.codingforanimals.veganuniverse.services.firebase.api.places.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import org.codingforanimals.veganuniverse.shared.entity.places.OpeningHours

data class PlaceFirebaseEntity(
    @DocumentId val id: String = "",
    val imageRef: String = "",
    val type: String = "",
    val name: String = "",
    val rating: Int = -1,
    val reviewsCount: Int = 0,
    val description: String = "",
    val address: String = "",
    val city: String = "",
    val tags: List<String> = emptyList(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val geoHash: String = "",
    val timestamp: Timestamp? = null,
    val openingHours: List<OpeningHours> = emptyList(),
) {
    /**
     * 'val seconds = timestamp?.seconds' returns always null
     */
    val timestampSeconds
        get() = timestamp?.seconds
}