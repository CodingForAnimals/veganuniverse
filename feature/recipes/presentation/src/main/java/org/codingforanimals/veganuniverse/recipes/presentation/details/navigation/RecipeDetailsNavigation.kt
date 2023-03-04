package org.codingforanimals.veganuniverse.recipes.presentation.details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.recipes.presentation.details.RecipeDetailsScreen

object RecipeDetailsDestination : Destination(route = "recipe_details")

fun NavGraphBuilder.recipeDetailsGraph(
    onBackClick: () -> Unit,
) {
    composable(
        route = RecipeDetailsDestination.route
    ) {
        RecipeDetailsScreen(
            onBackClick = onBackClick,
        )
    }
}