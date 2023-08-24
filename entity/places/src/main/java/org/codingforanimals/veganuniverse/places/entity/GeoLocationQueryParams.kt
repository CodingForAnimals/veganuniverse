package org.codingforanimals.veganuniverse.places.entity

data class GeoLocationQueryParams(
    val latitude: Double,
    val longitude: Double,
    val radiusKm: Double,
)