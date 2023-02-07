package org.codingforanimals.veganuniverse.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.presentation.RecipesScreen

object RecipesDestination: Destination(route = "recipes_route")

fun NavController.navigateToRecipes() {
    navigate(RecipesDestination.route) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }

}

fun NavGraphBuilder.recipesGraph() {
    composable(
        route = RecipesDestination.route,
    ) {
        RecipesScreen()
    }
}