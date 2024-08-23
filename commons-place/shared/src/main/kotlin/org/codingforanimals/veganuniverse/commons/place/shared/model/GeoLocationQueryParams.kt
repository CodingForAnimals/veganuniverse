package org.codingforanimals.veganuniverse.commons.place.shared.model

data class GeoLocationQueryParams(
    val latitude: Double,
    val longitude: Double,
    val radiusKm: Double,
)