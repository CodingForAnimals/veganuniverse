package org.codingforanimals.veganuniverse.create.presentation.place

import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType

data class PlaceFormViewEntity(
    val type: PlaceType,
    val name: String,
    val description: String,
    val openingHours: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val tags: List<PlaceTag>,
)