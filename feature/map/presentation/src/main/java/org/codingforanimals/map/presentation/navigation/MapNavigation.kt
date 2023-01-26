package org.codingforanimals.map.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import org.codingforanimals.map.presentation.MapScreen
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
        MapScreen(
            navigateToSite = navigateToSite,
            snackbarHostState = snackbarHostState,
        )
    }
    nestedGraphs()
}