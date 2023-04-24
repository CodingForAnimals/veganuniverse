package org.codingforanimals.veganuniverse.core.ui.place

import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons

enum class PlaceType(val label: String, val icon: Icon) {
    RESTAURANT(label = "Restaurante", icon = VUIcons.Restaurant),
    CAFE(label = "Café", icon = VUIcons.CoffeeMug),
    STORE(label = "Tienda/Negocio", icon = VUIcons.Store),
}