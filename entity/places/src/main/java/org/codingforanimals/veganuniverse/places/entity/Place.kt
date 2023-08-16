package org.codingforanimals.veganuniverse.places.entity

data class Place(
    val id: String,
    val name: String,
    val addressComponents: AddressComponents,
    val imageRef: String,
    val type: String,
    val description: String,
    val rating: Int,
    val reviewCount: Int,
    val tags: List<String>,
    val latitude: Double,
    val longitude: Double,
    val geoHash: String,
    val timestamp: Long,
    val openingHours: List<OpeningHours>,
)