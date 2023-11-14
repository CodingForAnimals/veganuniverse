package org.codingforanimals.veganuniverse.places.ui

import org.codingforanimals.veganuniverse.ui.icon.Icon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

enum class PlaceType(val label: String, val icon: Icon) {
    STORE(label = "Negocio / Tienda", icon = VUIcons.Store),
    RESTAURANT(label = "Restaurant", icon = VUIcons.Restaurant),
    CAFE(label = "Café", icon = VUIcons.CoffeeMug),
    BAR(label = "Bar", icon = VUIcons.Beer),
}