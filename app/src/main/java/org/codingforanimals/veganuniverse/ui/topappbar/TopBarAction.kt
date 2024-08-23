package org.codingforanimals.veganuniverse.ui.topappbar

import org.codingforanimals.veganuniverse.ui.icon.Icon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons


sealed class TopBarAction(
    val icon: Icon,
    val contentDescription: String,
) {

    data object SettingsTopBarAction : TopBarAction(
        icon = VUIcons.Settings,
        contentDescription = "Configuración",
    )
}