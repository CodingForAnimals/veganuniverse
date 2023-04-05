package org.codingforanimals.veganuniverse.recipes.presentation.category

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination

object RecipesCategoryDestination : Destination(route = "recipes_category_recipes")

fun NavGraphBuilder.recipesCategoryGraph(
    navigateToRecipeDetails: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable(
        route = RecipesCategoryDestination.route,
    ) {
        RecipesScreen(
            navigateToRecipeDetails = navigateToRecipeDetails,
            onBackClick = onBackClick,
        )
    }
}