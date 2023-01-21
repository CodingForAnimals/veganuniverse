package org.codingforanimals.map.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.map.presentation.MapScreen

const val mapNavigationRoute = "map_route"

fun NavController.navigateToMap() = navigate(mapNavigationRoute)

fun NavGraphBuilder.mapGraph(
    navigateToSite: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {

    composable(
        route = mapNavigationRoute
    ) {
        MapScreen(
            navigateToSite = navigateToSite,
        )
    }
    nestedGraphs()
}