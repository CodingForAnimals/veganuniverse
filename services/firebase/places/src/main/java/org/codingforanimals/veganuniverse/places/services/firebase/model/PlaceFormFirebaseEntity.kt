package org.codingforanimals.veganuniverse.places.services.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import org.codingforanimals.veganuniverse.places.entity.AddressComponents
import org.codingforanimals.veganuniverse.places.entity.OpeningHours

data class PlaceFormFirebaseEntity(
    val name: String,
    val addressComponents: AddressComponents,
    val openingHours: List<OpeningHours>,
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val tags: List<String>,
    val geoHash: String,
    @ServerTimestamp val timestamp: Timestamp? = null,
    val verified: Boolean = false,
)