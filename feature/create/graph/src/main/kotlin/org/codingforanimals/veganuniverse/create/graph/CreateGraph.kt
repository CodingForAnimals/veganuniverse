package org.codingforanimals.veganuniverse.create.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.create.home.persentation.CreateHomeScreen
import org.codingforanimals.veganuniverse.create.place.presentation.CreatePlaceScreen
import org.codingforanimals.veganuniverse.create.product.presentation.CreateProductScreen
import org.codingforanimals.veganuniverse.create.recipe.CreateRecipeScreen
import org.codingforanimals.veganuniverse.create.thank_you.presentation.ThankYouScreen
import org.codingforanimals.veganuniverse.ui.navigation.Destination

sealed class CreateDestination(route: String) : Destination(route) {
    data object Home : CreateDestination("create_home_screen")
    data object Recipe : CreateDestination("create_recipe_screen")
    data object Place : CreateDestination("create_place_screen")
    data object Product : CreateDestination("create_product_screen")
    data object ThankYou : CreateDestination("create_thank_you_screen")
}

fun NavGraphBuilder.createGraph(
    navController: NavController,
    navigateToAuthenticationScreen: (CreateDestination) -> Unit,
    navigateToPlaceDetailsScreen: (String) -> Unit,
) {
    composable(
        route = CreateDestination.Home.route,
    ) {
        CreateHomeScreen(
            navigateToAuthenticationScreen = { navigateToAuthenticationScreen(CreateDestination.Home) },
            navigateToCreatePlaceScreen = { navController.navigate(CreateDestination.Place.route) },
            navigateToCreateRecipeScreen = { navController.navigate(CreateDestination.Recipe.route) },
            navigateToCreateProductScreen = { navController.navigate(CreateDestination.Product.route) },
        )
    }

    composable(
        route = CreateDestination.Place.route,
    ) {
        CreatePlaceScreen(
            navigateToThankYouScreen = { navController.navigate(CreateDestination.ThankYou.route) },
            navigateToAlreadyExistingPlace = {},
            navigateToAuthenticationScreen = { navigateToAuthenticationScreen(CreateDestination.Place) },
            navigateUp = navController::navigateUp,
        )
    }

    composable(
        route = CreateDestination.Recipe.route,
    ) {
        CreateRecipeScreen(
            navigateToThankYouScreen = { navController.navigate(CreateDestination.ThankYou.route) },
            navigateToAuthenticationScreen = { navigateToAuthenticationScreen(CreateDestination.Recipe) },
            navigateUp = navController::navigateUp,
        )
    }

    composable(
        route = CreateDestination.Product.route,
    ) {
        CreateProductScreen(
            navigateUp = navController::navigateUp,
            navigateToThankYouScreen = { navController.navigate(CreateDestination.ThankYou.route) },
            navigateToAuthenticationScreen = { navigateToAuthenticationScreen(CreateDestination.Product) },
        )
    }

    composable(
        route = CreateDestination.ThankYou.route,
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