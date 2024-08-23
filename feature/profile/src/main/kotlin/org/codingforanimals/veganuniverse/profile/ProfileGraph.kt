package org.codingforanimals.veganuniverse.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreen
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination

sealed class ProfileDestination(route: String) : Destination(route = route) {
    data object Home : ProfileDestination("profile_home_screen")
}

fun NavGraphBuilder.profileGraph(
    navigateToRegister: () -> Unit,
    navController: NavController,
) {
    composable(
        route = ProfileDestination.Home.route
    ) {
        ProfileScreen(
            navigateUp = navController::navigateUp,
            navigateToAuthenticationPrompt = navigateToRegister,
        )
    }
}

