package org.codingforanimals.veganuniverse.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreen

sealed class ProfileDestination(route: String) : Destination(route = route) {
    data object Home : ProfileDestination("profile_home_screen")
}

fun NavGraphBuilder.profileGraph(
    navigateToRegister: () -> Unit,
    navigateToPlaceListing: (String) -> Unit,
    navigateToRecipeListing: (String) -> Unit,
) {
    composable(
        route = ProfileDestination.Home.route
    ) {
        ProfileScreen(
            navigateToAuthenticationPrompt = navigateToRegister,
            navigateToPlaceListing = navigateToPlaceListing,
            navigateToRecipeListing = navigateToRecipeListing,
        )
    }
}

