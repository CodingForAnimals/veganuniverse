package org.codingforanimals.veganuniverse.places.entity

data class Place(
    val geoHash: String,
    val name: String,
    val addressComponents: AddressComponents,
    val imageRef: String,
    val type: String,
    val description: String,
    val rating: Int,
    val tags: List<String>,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val openingHours: List<OpeningHours>,
)