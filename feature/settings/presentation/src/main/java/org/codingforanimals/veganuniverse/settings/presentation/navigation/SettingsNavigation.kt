package org.codingforanimals.veganuniverse.settings.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.settings.presentation.SettingsScreen
import org.codingforanimals.veganuniverse.ui.navigation.Destination

object SettingsDestination : Destination(route = "settings_route")

fun NavGraphBuilder.settingsGraph(
    onBackClick: () -> Unit,
) {
    composable(
        route = SettingsDestination.route,
    ) {
        SettingsScreen(
            onBackClick = onBackClick,
        )
    }
}