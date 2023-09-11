package org.codingforanimals.veganuniverse.places.presentation.home.entity

import com.google.maps.android.compose.MarkerState
import org.codingforanimals.veganuniverse.places.ui.entity.PlaceCard

data class PlaceCardViewEntity(
    val card: PlaceCard,
    val markerState: MarkerState,
)