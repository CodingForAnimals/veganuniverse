package org.codingforanimals.veganuniverse.commons.place.presentation.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.codingforanimals.veganuniverse.commons.place.presentation.R
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceTag

data class PlaceTagUI(
    @StringRes val label: Int,
    val icon: Icon,
)

@Composable
fun PlaceTag.toUI(): PlaceTagUI {
    return remember {
        when (this) {
            PlaceTag.GLUTEN_FREE -> PlaceTagUI(
                label = R.string.tag_label_gluten_free,
                icon = VUIcons.GlutenFree
            )

            PlaceTag.FULL_VEGAN -> PlaceTagUI(
                label = R.string.tag_label_full_vegan,
                icon = VUIcons.VeganLogo
            )

            PlaceTag.DELIVERY -> PlaceTagUI(
                label = R.string.tag_label_delivery,
                icon = VUIcons.Delivery
            )

            PlaceTag.TAKEAWAY -> PlaceTagUI(
                label = R.string.tag_label_take_away,
                icon = VUIcons.Bag
            )

            PlaceTag.DINE_IN -> PlaceTagUI(
                label = R.string.tag_label_dine_in,
                icon = VUIcons.Chairs
            )
        }
    }
}