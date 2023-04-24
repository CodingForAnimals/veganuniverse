package org.codingforanimals.places.presentation.home

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import org.codingforanimals.places.presentation.home.composables.PlaceMarker
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType

data class PlaceViewEntity(
    val id: String,
    val imageRef: String,
    val type: PlaceType,
    val tags: List<PlaceTag>,
    val name: String,
    val rating: Int,
    val address: String,
    val city: String,
    val state: MarkerState,
    val marker: PlaceMarker,
)

data class PlaceCardViewEntity(
    val id: String,
    val imageRef: String,
    val type: PlaceType?,
    val name: String,
    val rating: Int,
    val address: String,
    val city: String,
)

data class PlaceMarkerViewEntity(
    val id: String,
    val type: PlaceType?,
    val name: String,
    val location: LatLng,
    val state: MarkerState,
)