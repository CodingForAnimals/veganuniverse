package org.codingforanimals.veganuniverse.places.presentation.entity

import com.google.maps.android.compose.MarkerState
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.places.presentation.details.model.PlaceMarker

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
    val state: MarkerState,
    val marker: PlaceMarker,
) {
    val fullStreetAddress: String = "$streetAddress, $administrativeArea"
}