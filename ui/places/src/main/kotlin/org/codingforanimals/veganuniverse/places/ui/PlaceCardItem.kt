package org.codingforanimals.veganuniverse.places.ui

import org.codingforanimals.veganuniverse.ui.cards.CardItem

class PlaceCardItem(
    geoHash: String,
    imageRef: String,
    name: String,
    val rating: Int,
    val streetAddress: String,
    val administrativeArea: String,
    val type: PlaceType,
    val tags: List<PlaceTag>,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val marker: PlaceMarker,
) : CardItem(
    id = geoHash,
    title = name,
    imageRef = imageRef,
) {
    val fullStreetAddress: String = "$streetAddress, $administrativeArea"
}