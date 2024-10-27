package org.codingforanimals.veganuniverse.place.presentation.model

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.shared.model.PlaceType

data class PlaceTypeUI(
    @StringRes val label: Int,
    val icon: Icon,
)

fun PlaceType.toUI(): PlaceTypeUI {
    return when (this) {
        PlaceType.STORE -> PlaceTypeUI(
            label = R.string.place_type_store_label,
            icon = VUIcons.Store
        )

        PlaceType.RESTAURANT -> PlaceTypeUI(
            label = R.string.place_type_restaurant_label,
            icon = VUIcons.Restaurant
        )

        PlaceType.CAFE -> PlaceTypeUI(
            label = R.string.place_type_cafe_label,
            icon = VUIcons.CoffeeMug
        )

        PlaceType.BAR -> PlaceTypeUI(label = R.string.place_type_bar_label, icon = VUIcons.Beer)
    }
}
