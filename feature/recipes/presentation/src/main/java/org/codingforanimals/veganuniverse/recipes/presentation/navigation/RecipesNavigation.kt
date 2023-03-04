package org.codingforanimals.veganuniverse.recipes.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import org.codingforanimals.veganuniverse.recipes.presentation.details.navigation.RecipeDetailsDestination
import org.codingforanimals.veganuniverse.recipes.presentation.details.navigation.recipeDetailsGraph
import org.codingforanimals.veganuniverse.recipes.presentation.home.navigation.recipesHomeGraph
import org.codingforanimals.veganuniverse.recipes.presentation.recipes.navigation.RecipesDestination
import org.codingforanimals.veganuniverse.recipes.presentation.recipes.navigation.categoryRecipesGraph

fun NavGraphBuilder.recipesGraph(
    navController: NavController,
) {
    recipesHomeGraph(
        navigateToRecipes = { navController.navigate(RecipesDestination.route) }
    )
    categoryRecipesGraph(
        navigateToRecipeDetails = { navController.navigate(RecipeDetailsDestination.route) },
        onBackClick = navController::navigateUp,
    )
    recipeDetailsGraph(
        onBackClick = navController::navigateUp,
    )
}