package org.codingforanimals.veganuniverse.place.model

import java.util.Date

data class Place(
    val geoHash: String?,
    val userId: String?,
    val username: String?,
    val name: String?,
    val addressComponents: AddressComponents?,
    val imageUrl: String?,
    val type: PlaceType?,
    val description: String?,
    val rating: Double?,
    val tags: List<PlaceTag>?,
    val latitude: Double,
    val longitude: Double,
    val createdAt: Date?,
    val openingHours: List<OpeningHours>,
)

