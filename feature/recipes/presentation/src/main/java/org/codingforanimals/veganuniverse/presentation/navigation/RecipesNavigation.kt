package org.codingforanimals.veganuniverse.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.presentation.RecipesScreen

const val recipesNavigationRoute = "recipes_route"

fun NavController.navigateToRecipes() = navigate(recipesNavigationRoute)

fun NavGraphBuilder.recipesGraph() {
    composable(
        route = recipesNavigationRoute
    ) {
        RecipesScreen()
    }
}