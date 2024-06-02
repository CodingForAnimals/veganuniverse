package org.codingforanimals.veganuniverse.place.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import org.codingforanimals.veganuniverse.place.model.AddressComponents
import org.codingforanimals.veganuniverse.place.model.OpeningHours

internal data class PlaceFirestoreEntity(
    @DocumentId val geoHash: String? = null,
    val userId: String? = null,
    val username: String? = null,
    val name: String? = null,
    val nameLowercase: String? = null,
    val description: String? = null,
    val rating: Double = 0.0,
    val type: String? = null,
    val tags: List<String>? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val openingHours: List<OpeningHours>? = null,
    val addressComponents: AddressComponents? = null,
    val imageId: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
)
