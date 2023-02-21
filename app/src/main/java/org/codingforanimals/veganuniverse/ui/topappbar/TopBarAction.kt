package org.codingforanimals.veganuniverse.ui.topappbar

import org.codingforanimals.veganuniverse.core.ui.R

sealed class TopBarAction(
    val drawableRes: Int,
    val contentDescription: String,
) {
    object NotificationTopBarAction : TopBarAction(
        drawableRes = R.drawable.ic_notifications,
        contentDescription = "Notificaciones",
    )

    object SettingsTopBarAction : TopBarAction(
        drawableRes = R.drawable.ic_settings,
        contentDescription = "Configuración",
    )
}