package org.codingforanimals.veganuniverse.places.presentation.details.model

import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons

object Markers {
    val restaurantMarker = PlaceMarker(
        defaultIcon = VUIcons.MarkerRestaurant,
        selectedIcon = VUIcons.MarkerRestaurantSelected,
    )

    val storeMarker = PlaceMarker(
        defaultIcon = VUIcons.MarkerStore,
        selectedIcon = VUIcons.MarkerStoreSelected,
    )

    val cafeMarker = PlaceMarker(
        defaultIcon = VUIcons.MarkerCafe,
        selectedIcon = VUIcons.MarkerCafeSelected,
    )
}