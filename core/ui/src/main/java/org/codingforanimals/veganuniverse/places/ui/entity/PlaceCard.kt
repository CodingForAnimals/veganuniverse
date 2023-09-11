package org.codingforanimals.veganuniverse.places.ui.entity

import org.codingforanimals.veganuniverse.core.ui.place.PlaceMarker
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType

data class PlaceCard(
    val geoHash: String,
    val name: String,
    val rating: Int,
    val streetAddress: String,
    val administrativeArea: String,
    val type: PlaceType,
    val imageRef: String,
    val tags: List<PlaceTag>,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val marker: PlaceMarker,
) {
    val fullStreetAddress: String = "$streetAddress, $administrativeArea"
}