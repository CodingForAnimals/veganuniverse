package org.codingforanimals.veganuniverse.places.presentation.entity

import com.google.maps.android.compose.MarkerState
import org.codingforanimals.veganuniverse.places.entity.AddressComponents
import org.codingforanimals.veganuniverse.places.presentation.details.model.OpeningHours
import org.codingforanimals.veganuniverse.places.ui.PlaceMarker
import org.codingforanimals.veganuniverse.places.ui.PlaceTag
import org.codingforanimals.veganuniverse.places.ui.PlaceType

internal data class Place(
    val geoHash: String,
    val name: String,
    val addressComponents: AddressComponents,
    val imageRef: String,
    val description: String,
    val type: PlaceType,
    val tags: List<PlaceTag>,
    val rating: Int,
    val state: MarkerState,
    val marker: PlaceMarker,
    val timestamp: Long,
    val openingHours: List<OpeningHours>,
)