package org.codingforanimals.veganuniverse.ui.topappbar

import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VeganUniverseIcons


sealed class TopBarAction(
    val icon: Icon,
    val contentDescription: String,
) {
    object SearchTopBarAction : TopBarAction(
        icon = VeganUniverseIcons.Search,
        contentDescription = "Buscar"
    )

    object NotificationTopBarAction : TopBarAction(
        icon = VeganUniverseIcons.Notifications,
        contentDescription = "Notificaciones",
    )

    object SettingsTopBarAction : TopBarAction(
        icon = VeganUniverseIcons.Settings,
        contentDescription = "Configuración",
    )
}