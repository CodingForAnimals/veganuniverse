package org.codingforanimals.places.presentation.home

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.maps.android.compose.CameraPositionState
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination

object PlacesHomeDestination : Destination(route = "places_home_route")

fun NavGraphBuilder.placesHomeGraph(
    snackbarHostState: SnackbarHostState,
    cameraPositionState: CameraPositionState,
    navigateToPlaceDetails: () -> Unit,
) {
    composable(
        route = PlacesHomeDestination.route,
        content = {
            PlacesHomeScreen(
                snackbarHostState = snackbarHostState,
                cameraPositionState = cameraPositionState,
                onPlaceClick = navigateToPlaceDetails,
            )
        }
    )
}