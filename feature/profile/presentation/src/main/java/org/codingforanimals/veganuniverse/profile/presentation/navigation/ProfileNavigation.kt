package org.codingforanimals.veganuniverse.profile.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreen

object ProfileDestination : Destination(route = "profile_route")

fun NavGraphBuilder.profileGraph(
    navigateToRegister: () -> Unit,
) {
    composable(
        route = ProfileDestination.route
    ) {
        ProfileScreen(
            navigateToRegister = navigateToRegister,
        )
    }
}