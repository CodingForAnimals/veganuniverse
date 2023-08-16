package org.codingforanimals.places.presentation.entity

import com.google.maps.android.compose.MarkerState
import org.codingforanimals.places.presentation.details.model.OpeningHours
import org.codingforanimals.places.presentation.details.model.PlaceMarker
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.places.entity.AddressComponents

internal data class PlaceViewEntity(
    val id: String,
    val name: String,
    val addressComponents: AddressComponents,
    val imageRef: String,
    val description: String,
    val type: PlaceType,
    val tags: List<PlaceTag>,
    val rating: Int,
    val reviewCount: Int,
    val state: MarkerState,
    val marker: PlaceMarker,
    val timestamp: Long,
    val openingHours: List<OpeningHours>,
)