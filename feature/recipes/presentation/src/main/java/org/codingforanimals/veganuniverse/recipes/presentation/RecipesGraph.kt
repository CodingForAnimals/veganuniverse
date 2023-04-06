package org.codingforanimals.veganuniverse.recipes.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import org.codingforanimals.veganuniverse.recipes.presentation.category.RecipesCategoryDestination
import org.codingforanimals.veganuniverse.recipes.presentation.category.recipesCategoryGraph
import org.codingforanimals.veganuniverse.recipes.presentation.details.RecipeDetailsDestination
import org.codingforanimals.veganuniverse.recipes.presentation.details.recipeDetailsGraph
import org.codingforanimals.veganuniverse.recipes.presentation.home.recipesHomeGraph
import org.codingforanimals.veganuniverse.recipes.presentation.report.RecipeReportDialog

fun NavGraphBuilder.recipesGraph(
    navController: NavController,
) {
    recipesHomeGraph(
        navigateToRecipes = { navController.navigate(RecipesCategoryDestination.route) }
    )
    recipesCategoryGraph(
        navigateToRecipeDetails = { navController.navigate(RecipeDetailsDestination.route) },
        onBackClick = navController::navigateUp,
    )
    recipeDetailsGraph(
        onBackClick = navController::navigateUp,
        openDialog = { navController.navigate("report") }
    )

    dialog("report") {
        RecipeReportDialog(
            onCloseDialog = navController::navigateUp,
        )
    }
}