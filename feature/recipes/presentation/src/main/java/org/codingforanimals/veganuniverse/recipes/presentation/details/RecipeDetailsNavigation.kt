package org.codingforanimals.veganuniverse.recipes.presentation.details

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination

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