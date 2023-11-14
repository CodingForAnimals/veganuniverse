package org.codingforanimals.veganuniverse.recipes.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.RecipeBrowsingScreen
import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeScreen
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.RecipeDetailsScreen
import org.codingforanimals.veganuniverse.recipes.presentation.report.RecipeReportDialog
import org.codingforanimals.veganuniverse.ui.navigation.Destination

sealed class RecipesDestination(route: String) : Destination(route) {
    data object Home : RecipesDestination("feature_recipes_home")
    data object Details : RecipesDestination("feature_recipes_details")
    data object Browsing : RecipesDestination("feature_recipes_browsing")
}

fun NavGraphBuilder.recipesGraph(
    navController: NavController,
    navigateToAuthenticateScreen: () -> Unit,
) {
    composable(
        route = RecipesDestination.Home.route,
    ) {
        RecipesHomeScreen(
            navigateToRecipeBrowsing = { navController.navigate("${RecipesDestination.Browsing.route}?tag=${it.tag}&sorter=${it.sorter}") },
            navigateToRecipeDetails = { recipeId -> navController.navigate("${RecipesDestination.Details.route}/$recipeId") },
        )
    }

    composable(
        route = "${RecipesDestination.Browsing.route}?tag={tag}&sorter={sorter}",
        arguments = listOf(
            navArgument("tag") {
                type = NavType.StringType
                nullable = true
            },
            navArgument("sorter") {
                type = NavType.StringType
                nullable = true
            }
        ),
    ) {
        RecipeBrowsingScreen(
            navigateToRecipeDetails = { tag -> navController.navigate("${RecipesDestination.Details.route}/$tag") },
            onBackClick = navController::navigateUp,
        )
    }

    composable(
        route = "${RecipesDestination.Details.route}/{recipeId}",
        arguments = listOf(
            navArgument("recipeId") { type = NavType.StringType }
        )
    ) {
        RecipeDetailsScreen(
            onBackClick = navController::navigateUp,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
        )
    }

    dialog("report") {
        RecipeReportDialog(
            onCloseDialog = navController::navigateUp,
        )
    }
}

internal data class RecipeBrowsingNavArgs(
    val tag: String? = null,
    val sorter: String? = null,
)