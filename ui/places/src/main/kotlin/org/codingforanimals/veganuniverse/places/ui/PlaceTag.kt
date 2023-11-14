package org.codingforanimals.veganuniverse.places.ui

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.ui.icon.Icon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

enum class PlaceTag(@StringRes val label: Int, val icon: Icon) {
    GLUTEN_FREE(label = R.string.tag_label_gluten_free, icon = VUIcons.GlutenFree),
    FULL_VEGAN(label = R.string.tag_label_full_vegan, icon = VUIcons.VeganLogo),
    DELIVERY(label = R.string.tag_label_delivery, icon = VUIcons.Delivery),
    TAKEAWAY(label = R.string.tag_label_take_away, icon = VUIcons.Bag),
    DINE_IN(label = R.string.tag_label_dine_in, icon = VUIcons.Chairs)
}