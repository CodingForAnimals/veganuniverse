package org.codingforanimals.veganuniverse.recipes.presentation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination

object RecipesHomeDestination : Destination(route = "recipes_home_route")

fun NavGraphBuilder.recipesHomeGraph(
    navigateToRecipes: () -> Unit,
) {
    composable(
        route = RecipesHomeDestination.route,
    ) {
        RecipesHomeScreen(
            navigateToCategoryRecipes = navigateToRecipes,
        )
    }
}