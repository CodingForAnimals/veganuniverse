package org.codingforanimals.veganuniverse.create.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.create.home.persentation.CreateHomeScreen
import org.codingforanimals.veganuniverse.create.place.presentation.CreatePlaceScreen
import org.codingforanimals.veganuniverse.create.product.presentation.CreateProductScreen
import org.codingforanimals.veganuniverse.create.recipe.CreateRecipeScreen
import org.codingforanimals.veganuniverse.create.thank_you.presentation.ThankYouScreen

sealed class CreateDestination(route: String) : Destination(route) {
    data object Home : CreateDestination("create_home_screen")
    data object Recipe : CreateDestination("create_recipe_screen")
    data object Place : CreateDestination("create_place_screen")
    data object Product : CreateDestination("create_product_screen")
    data object ThankYou : CreateDestination("create_thank_you_screen")
}

fun NavGraphBuilder.createGraph(
    navController: NavController,
) {
    composable(
        route = CreateDestination.Home.route,
    ) {
        CreateHomeScreen()
    }

    composable(
        route = CreateDestination.Place.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DeepLink.CreatePlace.deeplink
            }
        )
    ) {
        CreatePlaceScreen(
            navigateUp = navController::navigateUp,
        )
    }

    composable(
        route = CreateDestination.Recipe.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DeepLink.CreateRecipe.deeplink
            }
        )
    ) {
        CreateRecipeScreen(
            navigateUp = navController::navigateUp,
        )
    }

    composable(
        route = CreateDestination.Product.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DeepLink.CreateProduct.deeplink
            }
        )
    ) {
        CreateProductScreen(
            navigateUp = navController::navigateUp,
        )
    }

    composable(
        route = CreateDestination.ThankYou.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DeepLink.ThankYou.deeplink
            }
        )
    ) {
        ThankYouScreen(
            navigateToCreateScreen = {
                navController.popBackStack(
                    route = CreateDestination.Home.route,
                    inclusive = false
                )
            }
        )
    }
}