package org.codingforanimals.veganuniverse.recipes.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import org.codingforanimals.veganuniverse.recipes.presentation.category.RecipeBrowsingDestination
import org.codingforanimals.veganuniverse.recipes.presentation.category.recipesCategoryGraph
import org.codingforanimals.veganuniverse.recipes.presentation.home.recipesHomeGraph
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.RecipeDestination
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.recipeGraph
import org.codingforanimals.veganuniverse.recipes.presentation.report.RecipeReportDialog

fun NavGraphBuilder.recipesGraph(
    navController: NavController,
) {
    recipesHomeGraph(
//        navigateToRecipes = { navController.navigate(RecipesCategoryDestination.navigationRoute(it)) },
        navigateToRecipeBrowsing = { navController.navigate("${RecipeBrowsingDestination.route}?tag=${it.tag}&sorter=${it.sorter}") },
        navigateToRecipe = { recipeId -> navController.navigate("${RecipeDestination.route}/$recipeId") }
    )
    recipesCategoryGraph(
        navigateToRecipeDetails = { tag -> navController.navigate("${RecipeDestination.route}/$tag") },
        onBackClick = navController::navigateUp,
    )
    recipeGraph(
        onBackClick = navController::navigateUp,
        openDialog = { navController.navigate("report") }
    )

    dialog("report") {
        RecipeReportDialog(
            onCloseDialog = navController::navigateUp,
        )
    }
}