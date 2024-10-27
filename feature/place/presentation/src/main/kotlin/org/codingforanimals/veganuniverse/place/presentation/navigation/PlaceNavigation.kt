package org.codingforanimals.veganuniverse.place.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.commons.ui.navigation.navigate
import org.codingforanimals.veganuniverse.place.presentation.create.CreatePlaceScreen
import org.codingforanimals.veganuniverse.place.presentation.details.PlaceDetailsScreen
import org.codingforanimals.veganuniverse.place.presentation.home.PlacesHomeScreen
import org.codingforanimals.veganuniverse.place.presentation.listing.PlaceListingScreen
import org.codingforanimals.veganuniverse.place.presentation.reviews.PlaceReviewsScreen

sealed class PlaceDestination(route: String) : Destination(route) {
    data object Home : PlaceDestination("place_home")
    data object Create : PlaceDestination("create_place_screen")
    data object Details : PlaceDestination("place_details") {
        const val APP_LINK = "${DeepLink.APP_LINKS_BASE_URL}/place"
        fun getAppLink(placeId: String) = "$APP_LINK/$placeId"
    }
    data object Reviews : PlaceDestination("place_reviews")
    data object Listing : PlaceDestination("place_listing")
}

internal const val selected_place_id = "selected_place_id_argument"
internal const val selected_place_name = "selected_place_name_argument"
internal const val selected_place_rating = "selected_place_rating_argument"

fun NavGraphBuilder.placesGraph(
    navController: NavController,
    navigateToThankYouScreen: () -> Unit,
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

    with(PlaceDestination.Details) {
        composable(
            route = "$route/{$selected_place_id}",
            arguments = listOf(
                navArgument(selected_place_id) { type = NavType.StringType },
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DeepLink.PlaceDetail.pathWithSchema}/{$selected_place_id}"
                },
                navDeepLink {
                    uriPattern = "$APP_LINK/{$selected_place_id}"
                }
            )
        ) {
            PlaceDetailsScreen(
                navigateUp = navController::navigateUp,
                navigateToReviewsScreen = { id, name, rating, userId ->
                    navController.navigate("${PlaceDestination.Reviews.route}/$id/$name/$rating")
                },
            )
        }
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
        )
    }

    composable(
        route = "${PlaceDestination.Listing.route}/{$LISTING_TYPE}",
        arguments = listOf(
            navArgument(LISTING_TYPE) {
                type = NavType.StringType
            },
        )
    ) { backstackEntry ->
        val listingType = backstackEntry.arguments?.getString(LISTING_TYPE)
        PlaceListingScreen(
            listingType = listingType,
            navigateUp = navController::navigateUp,
            navigateToPlaceDetails = { placeId ->
                navController.navigate("${PlaceDestination.Details.route}/$placeId")
            }
        )
    }

    composable(
        route = PlaceDestination.Create.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DeepLink.CreatePlace.deeplink
            }
        )
    ) {
        CreatePlaceScreen(
            navigateUp = navController::navigateUp,
            navigateToThankYouScreen = navigateToThankYouScreen,
        )
    }
}

fun NavHostController.navigateToPlaceListing(listingType: String) {
    navigate("${PlaceDestination.Listing.route}/$listingType")
}

internal const val LISTING_TYPE = "listing_type"
