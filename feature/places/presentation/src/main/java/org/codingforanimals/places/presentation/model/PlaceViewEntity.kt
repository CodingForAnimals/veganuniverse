package org.codingforanimals.places.presentation.model

import com.google.maps.android.compose.MarkerState
import org.codingforanimals.places.presentation.details.model.PlaceMarker
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType

data class PlaceViewEntity(
    val id: String,
    val imageRef: String,
    val description: String,
    val type: PlaceType,
    val tags: List<PlaceTag>,
    val name: String,
    val rating: Int,
    val reviewCount: Int,
    val address: String,
    val city: String,
    val state: MarkerState,
    val marker: PlaceMarker,
    val timestamp: Long,
    val openingHours: String,
)