package org.codingforanimals.veganuniverse.registration.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.registration.presentation.WelcomeScreen

object WelcomeDestination : Destination(route = "welcome_route")

fun NavGraphBuilder.registrationGraph(
    onLoginSuccess: (Destination) -> Unit,
) {
    composable(
        route = WelcomeDestination.route,
    ) {
        WelcomeScreen(onLoginSuccess = { onLoginSuccess(WelcomeDestination) })
    }
}