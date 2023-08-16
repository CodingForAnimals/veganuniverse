package org.codingforanimals.veganuniverse.places.entity

data class PlaceForm(
    val name: String,
    val addressComponents: AddressComponents,
    val openingHours: List<OpeningHours>,
    val description: String,
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val tags: List<String>,
)