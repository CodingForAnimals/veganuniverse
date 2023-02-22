package org.codingforanimals.veganuniverse.ui.topappbar

import org.codingforanimals.veganuniverse.core.ui.icons.VeganUniverseIcons


sealed class TopBarAction(
    val drawableRes: Int,
    val contentDescription: String,
) {
    object SearchTopBarAction : TopBarAction(
        drawableRes = VeganUniverseIcons.Search,
        contentDescription = "Buscar"
    )

    object NotificationTopBarAction : TopBarAction(
        drawableRes = VeganUniverseIcons.Notifications,
        contentDescription = "Notificaciones",
    )

    object SettingsTopBarAction : TopBarAction(
        drawableRes = VeganUniverseIcons.Settings,
        contentDescription = "Configuración",
    )
}