package org.codingforanimals.veganuniverse.place.shared.model

data class PlaceCard(
    val geoHash: String,
    val name: String?,
    val rating: Double?,
    val streetAddress: String?,
    val administrativeArea: String?,
    val type: PlaceType?,
    val imageUrl: String?,
    val tags: List<PlaceTag>?,
    val latitude: Double,
    val longitude: Double,
)