package org.codingforanimals.map.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.map.presentation.MapRoute
import org.codingforanimals.map.presentation.di.injectMapModules

const val mapNavigationRoute = "map_route"

fun NavController.navigateToMap() = navigate(mapNavigationRoute)

fun NavGraphBuilder.mapGraph(
    navigateToSite: () -> Unit,
    snackbarHostState: SnackbarHostState,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    injectMapModules()
    composable(
        route = mapNavigationRoute
    ) {
        MapRoute(
            navigateToSite = navigateToSite,
            snackbarHostState = snackbarHostState
        )
    }
    nestedGraphs()
}