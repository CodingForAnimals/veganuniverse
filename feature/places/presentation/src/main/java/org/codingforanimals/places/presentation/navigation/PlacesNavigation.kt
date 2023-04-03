package org.codingforanimals.places.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.maps.android.compose.CameraPositionState
import org.codingforanimals.places.presentation.PlacesScreen
import org.codingforanimals.places.presentation.di.injectPlacesModule
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination

object PlacesDestination : Destination(route = "places_route")

fun NavGraphBuilder.placesGraph(
    snackbarHostState: SnackbarHostState,
    cameraPositionState: CameraPositionState,
) {
    injectPlacesModule()
    composable(
        route = PlacesDestination.route,
        content = {
            PlacesScreen(
                snackbarHostState = snackbarHostState,
                cameraPositionState = cameraPositionState,
            )
        }
    )
}