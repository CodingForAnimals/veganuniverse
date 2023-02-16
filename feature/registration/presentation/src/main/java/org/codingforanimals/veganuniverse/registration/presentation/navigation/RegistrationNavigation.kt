package org.codingforanimals.veganuniverse.registration.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.registration.presentation.di.registrationModule
import org.codingforanimals.veganuniverse.registration.presentation.register.RegisterScreen
import org.koin.core.context.loadKoinModules

object RegisterDestination : Destination(route = "register_route")

fun NavController.navigateToRegister() = navigate(RegisterDestination.route)

fun NavGraphBuilder.registrationGraph(
    navigateToCommunity: () -> Unit,
) {
    loadKoinModules(registrationModule)
    composable(
        route = RegisterDestination.route,
    ) {
        RegisterScreen(
            navigateToCommunity = navigateToCommunity,
        )
    }
}