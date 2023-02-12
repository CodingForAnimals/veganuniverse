package org.codingforanimals.veganuniverse.profile.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreen
import org.codingforanimals.veganuniverse.profile.presentation.di.profileModule
import org.koin.core.context.loadKoinModules

object ProfileDestination : Destination(route = "profile_route")

fun NavGraphBuilder.profileGraph(
    navigateToRegister: () -> Unit,
) {
    loadKoinModules(profileModule)
    composable(
        route = ProfileDestination.route
    ) {
        ProfileScreen(
            navigateToRegister = navigateToRegister,
        )
    }
}