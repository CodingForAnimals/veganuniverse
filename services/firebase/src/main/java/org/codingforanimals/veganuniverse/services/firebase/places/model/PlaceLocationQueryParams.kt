package org.codingforanimals.veganuniverse.services.firebase.places.model

data class PlaceLocationQueryParams(
    val latitude: Double,
    val longitude: Double,
    val radiusInMeters: Double,
)