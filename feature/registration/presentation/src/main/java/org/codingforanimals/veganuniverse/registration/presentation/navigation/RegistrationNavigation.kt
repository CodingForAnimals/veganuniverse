package org.codingforanimals.veganuniverse.registration.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.registration.presentation.WelcomeScreen
import org.codingforanimals.veganuniverse.registration.presentation.register.RegisterScreen

object WelcomeDestination : Destination(route = "welcome_route")
object RegisterDestination : Destination(route = "register_route")

fun NavGraphBuilder.registrationGraph(
    navController: NavHostController,
    onLoginSuccess: (Destination) -> Unit,
) {
    composable(
        route = WelcomeDestination.route,
    ) {
        WelcomeScreen(
            onLoginSuccess = { onLoginSuccess(WelcomeDestination) },
        )
    }

    composable(
        route = RegisterDestination.route,
    ) {
        RegisterScreen()
    }
}