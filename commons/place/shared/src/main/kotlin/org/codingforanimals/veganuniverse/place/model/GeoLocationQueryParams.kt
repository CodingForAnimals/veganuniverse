package org.codingforanimals.veganuniverse.place.model

data class GeoLocationQueryParams(
    val latitude: Double,
    val longitude: Double,
    val radiusKm: Double,
)
