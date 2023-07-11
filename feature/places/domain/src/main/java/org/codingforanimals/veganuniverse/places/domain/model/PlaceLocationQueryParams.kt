package org.codingforanimals.veganuniverse.places.domain.model

data class PlaceLocationQueryParams(
    val latitude: Double,
    val longitude: Double,
    val radiusInMeters: Double,
)