package org.codingforanimals.veganuniverse.places.presentation.home.entity

import com.google.maps.android.compose.MarkerState
import org.codingforanimals.veganuniverse.places.ui.PlaceCardItem

data class PlaceCardViewEntity(
    val card: PlaceCardItem,
    val markerState: MarkerState,
)
