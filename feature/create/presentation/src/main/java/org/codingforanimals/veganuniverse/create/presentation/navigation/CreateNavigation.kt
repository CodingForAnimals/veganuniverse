package org.codingforanimals.veganuniverse.create.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.create.presentation.CreateScreen
import org.codingforanimals.veganuniverse.create.presentation.place.createPlaceModule
import org.koin.core.context.loadKoinModules

object CreateDestination : Destination(route = "create_route")

fun NavGraphBuilder.createGraph() {
    loadKoinModules(createPlaceModule)
    composable(
        route = CreateDestination.route
    ) {
        CreateScreen()
    }
}