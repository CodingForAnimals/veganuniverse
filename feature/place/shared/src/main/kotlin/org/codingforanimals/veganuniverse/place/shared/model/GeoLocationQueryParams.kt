package org.codingforanimals.veganuniverse.place.shared.model

data class GeoLocationQueryParams(
    val latitude: Double,
    val longitude: Double,
    val radiusKm: Double,
)