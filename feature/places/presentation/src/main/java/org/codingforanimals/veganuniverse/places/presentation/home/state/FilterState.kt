package org.codingforanimals.veganuniverse.places.presentation.home.state

import org.codingforanimals.veganuniverse.core.ui.place.PlaceSorter
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.places.presentation.home.PlacesHomeViewModel

internal data class FilterState(
    val activePlaceType: PlaceType? = null,
    val activePlaceTags: List<PlaceTag> = emptyList(),
    val sorter: PlaceSorter = PlaceSorter.RATING,
    val visibleDialog: PlacesHomeViewModel.FilterDialog? = null,
) {
    val isFilterActive: Boolean
        get() = activePlaceType != null || activePlaceTags.isNotEmpty()
}