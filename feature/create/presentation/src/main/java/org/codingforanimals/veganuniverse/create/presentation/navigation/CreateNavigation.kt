package org.codingforanimals.veganuniverse.create.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.create.presentation.CreateScreen

object CreateDestination : Destination(route = "create_route")

fun NavGraphBuilder.createGraph() {
    composable(
        route = CreateDestination.route
    ) {
        CreateScreen()
    }
}