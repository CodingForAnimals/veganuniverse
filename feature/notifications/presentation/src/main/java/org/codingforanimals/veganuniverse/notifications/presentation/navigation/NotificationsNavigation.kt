package org.codingforanimals.veganuniverse.notifications.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.notifications.presentation.NotificationsScreen
import org.codingforanimals.veganuniverse.ui.navigation.Destination

object NotificationsDestination : Destination(route = "notifications_route")

fun NavGraphBuilder.notificationsGraph(
    onBackClick: () -> Unit,
) {
    composable(
        route = NotificationsDestination.route,
    ) {
        NotificationsScreen(
            onBackClick = onBackClick,
        )
    }
}