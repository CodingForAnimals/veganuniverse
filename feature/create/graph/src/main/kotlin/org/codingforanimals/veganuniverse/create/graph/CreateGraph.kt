package org.codingforanimals.veganuniverse.create.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import org.codingforanimals.veganuniverse.commons.create.domain.model.CreateFeature
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.commons.ui.navigation.navigate
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
    data class ThankYou(
        val contributionType: CreateFeature
    ) : CreateDestination("$ROUTE?$CONTRIBUTION_TYPE=${contributionType.name}") {
        companion object {
            internal const val ROUTE = "create_thank_you_screen"
            internal const val CONTRIBUTION_TYPE = "contribution-type"
        }
    }
}

fun NavGraphBuilder.createGraph(
    navController: NavController,
    navigateToProfileScreen: () -> Unit,
) {
    composable(
        route = CreateDestination.Home.route,
    ) {
        CreateHomeScreen(
            navigateUp = navController::navigateUp,
        )
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
            navigateToThankYouScreen = {
                navController.navigate(CreateDestination.ThankYou(CreateFeature.PLACE))
            }
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
            navigateToThankYouScreen = {
                navController.navigate(CreateDestination.ThankYou(CreateFeature.RECIPE))
            }
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
            navigateToThankYouScreen = {
                navController.navigate(CreateDestination.ThankYou(CreateFeature.PRODUCT))
            }
        )
    }

    with(CreateDestination.ThankYou) {
        composable(
            route = "$ROUTE?$CONTRIBUTION_TYPE={$CONTRIBUTION_TYPE}",
            arguments = listOf(
                navArgument(CONTRIBUTION_TYPE) {
                    type = NavType.StringType
                }
            ),
        ) {
            val contributionType =
                it.arguments?.getString(CONTRIBUTION_TYPE)?.let { contributionType ->
                    CreateFeature.fromString(contributionType)
                }
            ThankYouScreen(
                navigateToCreateScreen = {
                    navController.popBackStack(
                        route = CreateDestination.Home.route,
                        inclusive = false
                    )
                },
                contributionType = contributionType,
                navigateToProfileScreen = navigateToProfileScreen
            )
        }
    }
}