package org.codingforanimals.veganuniverse.ui.topappbar

import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons


sealed class TopBarAction(
    val icon: Icon,
    val contentDescription: String,
) {
    object SearchTopBarAction : TopBarAction(
        icon = VUIcons.Search,
        contentDescription = "Buscar"
    )

    object NotificationTopBarAction : TopBarAction(
        icon = VUIcons.Notifications,
        contentDescription = "Notificaciones",
    )

    object SettingsTopBarAction : TopBarAction(
        icon = VUIcons.Settings,
        contentDescription = "Configuración",
    )
}