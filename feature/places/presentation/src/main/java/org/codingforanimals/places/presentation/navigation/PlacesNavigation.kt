package org.codingforanimals.places.presentation.navigation

import android.os.Bundle
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import org.codingforanimals.places.presentation.PlacesScreen
import org.codingforanimals.places.presentation.di.injectPlacesModule
import org.json.JSONStringer

const val placesNavigationRoute = "places_route"

fun NavController.navigateToPlaces() = navigate(placesNavigationRoute)

fun NavGraphBuilder.placesGraph(
    snackbarHostState: SnackbarHostState,
    cameraPositionState: CameraPositionState,
    navigateToMap: (Double, Double, Float) -> Unit,
) {
    injectPlacesModule()
    composable(
        route = placesNavigationRoute,
        content = {
            PlacesScreen(
                snackbarHostState = snackbarHostState,
                cameraPositionState = cameraPositionState,
                navigateToMap = navigateToMap,
            )
        }
    )
}