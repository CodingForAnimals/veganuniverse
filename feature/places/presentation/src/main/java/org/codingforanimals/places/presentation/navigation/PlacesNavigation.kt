package org.codingforanimals.places.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.codingforanimals.places.presentation.details.PlaceDetailsScreen
import org.codingforanimals.places.presentation.home.PlacesHomeScreen
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination

sealed class PlacesDestination(route: String) : Destination(route) {
    object Home : PlacesDestination("places_home")
    object Details : PlacesDestination("places_details")
}

internal const val selected_place_id = "selected_place_id_argument"

fun NavGraphBuilder.placesGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {
    composable(
        route = PlacesDestination.Home.route,
        content = {
            PlacesHomeScreen(
                snackbarHostState = snackbarHostState,
                navigateToPlaceDetails = { placeId ->
                    navController.navigate("${PlacesDestination.Details.route}/$placeId")
                },
                navigateUp = navController::navigateUp,
            )
        }
    )

    composable(
        route = "${PlacesDestination.Details.route}/{$selected_place_id}",
        arguments = listOf(navArgument(selected_place_id) { type = NavType.StringType })
    ) {
        PlaceDetailsScreen(
            onBackClick = navController::navigateUp,
        )
    }
}