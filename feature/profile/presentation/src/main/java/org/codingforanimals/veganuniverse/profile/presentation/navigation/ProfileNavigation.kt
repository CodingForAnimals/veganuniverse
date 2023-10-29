package org.codingforanimals.veganuniverse.profile.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.profile.presentation.ProfileScreen

object ProfileDestination : Destination(route = "profile_route")

fun NavGraphBuilder.profileGraph(
    navigateToRegister: () -> Unit,
    navController: NavController,
) {
    composable(
        route = ProfileDestination.route
    ) {
        ProfileScreen(
            navigateToRegister = navigateToRegister,
            test = { navController.navigate("route2") }
        )
    }

    composable(
        route = "route2",
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "testing", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}