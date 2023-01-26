package org.codingforanimals.places.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.maps.android.compose.CameraPositionState
import org.codingforanimals.places.presentation.PlacesScreen

const val placesNavigationRoute = "places_route"

fun NavController.navigateToPlaces() = navigate(placesNavigationRoute)

fun NavGraphBuilder.placesGraph(
    cameraPositionState: CameraPositionState,
    navigateToMap: () -> Unit,
) {
    composable(
        route = placesNavigationRoute,
        content = {
            PlacesScreen(
                cameraPositionState = cameraPositionState,
                navigateToMap = navigateToMap,
            )
        }
    )
}