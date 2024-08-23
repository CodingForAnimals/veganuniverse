package org.codingforanimals.veganuniverse.place.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsScreen
import org.codingforanimals.veganuniverse.place.presentation.home.PlacesHomeScreen
import org.codingforanimals.veganuniverse.place.presentation.reviews.PlaceReviewsScreen
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination

sealed class PlaceDestination(route: String) : Destination(route) {
    data object Home : PlaceDestination("place_home")
    data object Details : PlaceDestination("place_details")
    data object Reviews : PlaceDestination("place_reviews")
}

internal const val selected_place_id = "selected_place_id_argument"
internal const val selected_place_name = "selected_place_name_argument"
internal const val selected_place_rating = "selected_place_rating_argument"

fun NavGraphBuilder.placesGraph(
    navController: NavController,
    navigateToAuthenticateScreen: () -> Unit,
    navigatoToReauthenticateScreen: () -> Unit,
) {
    composable(
        route = PlaceDestination.Home.route,
        content = {
            PlacesHomeScreen(
                navigateUp = navController::navigateUp,
                navigateToPlaceDetails = { placeId ->
                    navController.navigate("${PlaceDestination.Details.route}/$placeId")
                },
            )
        }
    )

    composable(
        route = "${PlaceDestination.Details.route}/{$selected_place_id}",
        arguments = listOf(
            navArgument(selected_place_id) { type = NavType.StringType },
        )
    ) {
        PlaceDetailsScreen(
            navigateUp = navController::navigateUp,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            navigateToReviewsScreen = { id, name, rating, userId ->
                navController.navigate("${PlaceDestination.Reviews.route}/$id/$name/$rating")
            },
            navigatoToReauthenticateScreen = navigatoToReauthenticateScreen,
        )
    }

    composable(
        route = "${PlaceDestination.Reviews.route}/{$selected_place_id}/{$selected_place_name}/{$selected_place_rating}",
        arguments = listOf(
            navArgument(selected_place_id) { type = NavType.StringType },
            navArgument(selected_place_name) { type = NavType.StringType },
            navArgument(selected_place_rating) { type = NavType.StringType },
        ),
    ) {
        PlaceReviewsScreen(
            navigateUp = navController::navigateUp,
            placeName = it.arguments?.getString(selected_place_name),
            rating = it.arguments?.getString(selected_place_rating)?.toInt(),
            navigateToAuthenticationScreen = navigateToAuthenticateScreen,
        )
    }
}