package org.codingforanimals.veganuniverse.recipes.presentation.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeScreen

object RecipeCategoriesDestination : Destination(route = "recipes_route")

fun NavGraphBuilder.recipesHomeGraph(
    navigateToRecipes: () -> Unit,
) {
    composable(
        route = RecipeCategoriesDestination.route,
    ) {
        RecipesHomeScreen(
            navigateToCategoryRecipes = navigateToRecipes,
        )
    }
}