package org.codingforanimals.veganuniverse.services.firebase.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

internal data class Place(
    @DocumentId val geoHash: String = "",
    val name: String = "",
    val rating: Int = 0,
    val type: String = "",
    val tags: List<String> = emptyList(),
    val latitude: Double = -1.0,
    val longitude: Double = -1.0,
    val openingHours: List<OpeningHours> = emptyList(),
    val description: String = "",
    val addressComponents: AddressComponents = AddressComponents(),
    @ServerTimestamp val timestamp: Timestamp? = null,
)