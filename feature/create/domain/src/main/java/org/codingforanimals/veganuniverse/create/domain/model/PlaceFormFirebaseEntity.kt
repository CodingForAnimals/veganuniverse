package org.codingforanimals.veganuniverse.create.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class PlaceFormFirebaseEntity(
    val name: String,
    val openingHours: String,
    val type: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val city: String,
    val tags: List<String>,
    val geoHash: String,
    @ServerTimestamp val timestamp: Timestamp? = null,
)