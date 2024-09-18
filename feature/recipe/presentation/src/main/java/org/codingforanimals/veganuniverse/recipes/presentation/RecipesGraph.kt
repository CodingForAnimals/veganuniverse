package org.codingforanimals.veganuniverse.recipes.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeSorter
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeTag
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.commons.ui.navigation.navigate
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.RecipeBrowsingScreen
import org.codingforanimals.veganuniverse.recipes.presentation.details.RecipeDetailsScreen
import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeScreen
import org.codingforanimals.veganuniverse.recipes.presentation.listing.RecipeListingScreen

sealed class RecipesDestination(route: String) : Destination(route) {
    data object Home : RecipesDestination("feature_recipes_home")
    data object Details : RecipesDestination("feature_recipes_details")
    data class Browsing(
        val tag: String? = null,
        val sorter: String? = null,
    ) : RecipesDestination("$ROUTE?$TAG=$tag&$SORTER=$sorter") {
        companion object {
            const val ROUTE = "feature_recipes_browsing"
            internal const val TAG = "tag"
            internal const val SORTER = "sorter"
        }
    }

    data object Listing : RecipesDestination("feature_recipes_listing")
}

fun NavGraphBuilder.recipesGraph(
    navController: NavController,
) {
    composable(
        route = RecipesDestination.Home.route,
    ) {
        RecipesHomeScreen(
            navigateUp = navController::navigateUp,
            navigateToRecipeBrowsing = {
                navController.navigate(RecipesDestination.Browsing(it.tag?.name, it.sorter?.name))
            },
            navigateToRecipeDetails = { recipeId -> navController.navigateToRecipeDetails(recipeId) },
        )
    }

    with(RecipesDestination.Browsing) {
        composable(
            route = "$ROUTE?$TAG={$TAG}&$SORTER={$SORTER}",
            arguments = listOf(
                navArgument(TAG) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(SORTER) {
                    type = NavType.StringType
                    nullable = true
                }
            ),
        ) {
            RecipeBrowsingScreen(
                navigateToRecipeDetails = { id -> navController.navigateToRecipeDetails(id) },
                onBackClick = navController::navigateUp,
            )
        }
    }

    composable(
        route = "${RecipesDestination.Details.route}/{$RECIPE_ID}",
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "https://veganuniverse-a924e .web.app/recipe/{$RECIPE_ID}"
            }
        ),
        arguments = listOf(
            navArgument(RECIPE_ID) { type = NavType.StringType }
        )
    ) {
        RecipeDetailsScreen(
            navigateUp = navController::navigateUp,
        )
    }

    composable(
        route = "${RecipesDestination.Listing.route}/{$LISTING_TYPE}",
        arguments = listOf(
            navArgument(LISTING_TYPE) {
                type = NavType.StringType
            }
        )
    ) {
        RecipeListingScreen(
            navigateUp = navController::navigateUp,
            navigateToRecipeDetails = { id -> navController.navigateToRecipeDetails(id) }
        )
    }
}

private fun NavController.navigateToRecipeDetails(id: String) {
    navigate("${RecipesDestination.Details.route}/$id")
}

fun NavController.navigateToRecipeListing(listingType: String) {
    navigate("${RecipesDestination.Listing.route}/$listingType")
}

internal data class RecipeBrowsingNavArgs(
    val tag: RecipeTag? = null,
    val sorter: RecipeSorter? = null,
)

internal const val LISTING_TYPE = "listing-type"
internal const val RECIPE_ID = "recipe-id"
