package org.codingforanimals.veganuniverse.services.firebase.api.places.model

data class PlaceLocationQueryParams(
    val latitude: Double,
    val longitude: Double,
    val radiusInMeters: Double,
)