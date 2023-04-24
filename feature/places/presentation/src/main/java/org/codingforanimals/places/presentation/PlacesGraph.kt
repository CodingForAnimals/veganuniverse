package org.codingforanimals.places.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.maps.android.compose.CameraPositionState
import org.codingforanimals.places.presentation.details.PlaceDetailsDestination
import org.codingforanimals.places.presentation.details.placeDetailsGraph
import org.codingforanimals.places.presentation.di.injectPlacesModule
import org.codingforanimals.places.presentation.home.placesHomeGraph

fun NavGraphBuilder.placesGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    cameraPositionState: CameraPositionState,
) {
    injectPlacesModule()
    placesHomeGraph(
        snackbarHostState = snackbarHostState,
        cameraPositionState = cameraPositionState,
        navigateToPlaceDetails = { navController.navigate(PlaceDetailsDestination.route) },
    )

    placeDetailsGraph(
        onBackClick = navController::navigateUp
    )
}