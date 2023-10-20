package org.codingforanimals.veganuniverse.recipes.presentation.browsing

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination

object RecipeBrowsingDestination : Destination(route = "recipe_browsing")

fun NavGraphBuilder.recipesCategoryGraph(
    navigateToRecipeDetails: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    composable(
        route = "${RecipeBrowsingDestination.route}?tag={tag}&sorter={sorter}",
        arguments = listOf(
            navArgument("tag") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument("sorter") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        ),
    ) {
        RecipeBrowsingScreen(
            navigateToRecipeDetails = navigateToRecipeDetails,
            onBackClick = onBackClick,
        )
    }
}

data class RecipeBrowsingNavArgs(
    val tag: String? = null,
    val sorter: String? = null,
)