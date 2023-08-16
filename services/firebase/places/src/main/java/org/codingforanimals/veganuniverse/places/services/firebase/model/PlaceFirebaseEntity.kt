package org.codingforanimals.veganuniverse.places.services.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import org.codingforanimals.veganuniverse.places.entity.AddressComponents
import org.codingforanimals.veganuniverse.places.entity.OpeningHours

internal data class PlaceFirebaseEntity(
    @DocumentId val id: String = "",
    val name: String = "",
    val addressComponents: AddressComponents = AddressComponents(),
    val imageRef: String = "",
    val type: String = "",
    val rating: Int = -1,
    val reviewsCount: Int = 0,
    val description: String = "",
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
        get() = timestamp?.seconds ?: -1
}