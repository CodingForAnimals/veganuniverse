package org.codingforanimals.veganuniverse.recipes.presentation.recipes.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.recipes.presentation.recipes.RecipesScreen

object RecipesDestination : Destination(route = "category_recipes")

fun NavGraphBuilder.categoryRecipesGraph(
    navigateToRecipeDetails: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable(
        route = RecipesDestination.route,
    ) {
        RecipesScreen(
            navigateToRecipeDetails = navigateToRecipeDetails,
            onBackClick = onBackClick,
        )
    }
}