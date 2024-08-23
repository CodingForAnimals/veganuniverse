package org.codingforanimals.veganuniverse.place.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.ui.icon.Icon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

data class PlageTagUI(
    @StringRes val label: Int,
    val icon: Icon,
)

@Composable
fun PlaceTag.toUI(): PlaceTypeUI {
    return remember {
        when (this) {
            PlaceTag.GLUTEN_FREE -> PlaceTypeUI(
                label = R.string.tag_label_gluten_free,
                icon = VUIcons.GlutenFree
            )

            PlaceTag.FULL_VEGAN -> PlaceTypeUI(
                label = R.string.tag_label_full_vegan,
                icon = VUIcons.VeganLogo
            )

            PlaceTag.DELIVERY -> PlaceTypeUI(
                label = R.string.tag_label_delivery,
                icon = VUIcons.Delivery
            )

            PlaceTag.TAKEAWAY -> PlaceTypeUI(
                label = R.string.tag_label_take_away,
                icon = VUIcons.Bag
            )

            PlaceTag.DINE_IN -> PlaceTypeUI(
                label = R.string.tag_label_dine_in,
                icon = VUIcons.Chairs
            )
        }
    }
}