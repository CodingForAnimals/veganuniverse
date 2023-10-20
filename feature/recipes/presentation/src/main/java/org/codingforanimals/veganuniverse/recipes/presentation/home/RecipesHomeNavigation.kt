package org.codingforanimals.veganuniverse.recipes.presentation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.RecipeBrowsingNavArgs

object RecipesHomeDestination : Destination(route = "recipes_home_route")

fun NavGraphBuilder.recipesHomeGraph(
    navigateToRecipeBrowsing: (RecipeBrowsingNavArgs) -> Unit,
    navigateToRecipe: (String) -> Unit,
) {
    composable(
        route = RecipesHomeDestination.route,
    ) {
        RecipesHomeScreen(
            navigateToRecipeBrowsing = navigateToRecipeBrowsing,
            navigateToRecipe = navigateToRecipe,
        )
    }
}