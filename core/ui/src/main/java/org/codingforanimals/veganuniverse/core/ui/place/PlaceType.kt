package org.codingforanimals.veganuniverse.core.ui.place

import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons

enum class PlaceType(val label: String, val icon: Icon) {
    STORE(label = "Negocio / Tienda", icon = VUIcons.Store),
    RESTAURANT(label = "Restaurant", icon = VUIcons.Restaurant),
    CAFE(label = "Café", icon = VUIcons.CoffeeMug),
    BAR(label = "Bar", icon = VUIcons.Beer),
}