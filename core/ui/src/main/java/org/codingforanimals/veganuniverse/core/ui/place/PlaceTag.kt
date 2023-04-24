package org.codingforanimals.veganuniverse.core.ui.place

import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons

enum class PlaceTag(val label: String, val icon: Icon) {
    GLUTEN_FREE(label = "Sin tacc", icon = VUIcons.GlutenFree),
    FULL_VEGAN(label = "100% vegano", icon = VUIcons.VeganLogo),
    DELIVERY(label = "Delivery", icon = VUIcons.Delivery),
    TAKEAWAY(label = "Take away", icon = VUIcons.Bag),
    DINE_IN(label = "Consumo en el lugar", icon = VUIcons.Chairs)
}