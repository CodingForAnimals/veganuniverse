package org.codingforanimals.veganuniverse.recipes.presentation.recipe

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination

object RecipeDestination : Destination(route = "recipe_details")

fun NavGraphBuilder.recipeGraph(
    onBackClick: () -> Unit,
    openDialog: () -> Unit,
) {
    composable(
        route = "${RecipeDestination.route}/{recipeId}",
        arguments = listOf(
            navArgument("recipeId") { type = NavType.StringType }
        )
    ) {
        RecipeScreen(
            onBackClick = onBackClick,
            openDialog = openDialog,
        )
    }
}