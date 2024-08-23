package org.codingforanimals.veganuniverse.recipes.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeSorter
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeTag
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.commons.ui.navigation.navigate
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.RecipeBrowsingScreen
import org.codingforanimals.veganuniverse.recipes.presentation.details.RecipeDetailsScreen
import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeScreen
import org.codingforanimals.veganuniverse.recipes.presentation.listing.RecipeListingScreen
import org.codingforanimals.veganuniverse.recipes.presentation.report.RecipeReportDialog
import org.koin.androidx.compose.koinViewModel

sealed class RecipesDestination(route: String) : Destination(route) {
    data object Home : RecipesDestination("feature_recipes_home")
    data object Details : RecipesDestination("feature_recipes_details")
    data class Browsing(
        val tag: String? = null,
        val sorter: String? = null,
    ) : RecipesDestination("$ROUTE?$TAG=$tag&$SORTER=$sorter") {
        companion object {
            const val ROUTE = "feature_recipes_browsing"
            internal const val TAG = "tag"
            internal const val SORTER = "sorter"
        }
    }

    data object Listing : RecipesDestination("feature_recipes_listing")
}

fun NavGraphBuilder.recipesGraph(
    navController: NavController,
    navigateToAuthenticateScreen: () -> Unit,
) {
    composable(
        route = RecipesDestination.Home.route,
    ) {
        RecipesHomeScreen(
            navigateToRecipeBrowsing = {
                navController.navigate(RecipesDestination.Browsing(it.tag?.name, it.sorter?.name))
            },
            navigateToRecipeDetails = { recipeId -> navController.navigateToRecipeDetails(recipeId) },
        )
    }

    with(RecipesDestination.Browsing) {
        composable(
            route = "$ROUTE?$TAG={$TAG}&$SORTER={$SORTER}",
            arguments = listOf(
                navArgument(TAG) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(SORTER) {
                    type = NavType.StringType
                    nullable = true
                }
            ),
        ) {
            RecipeBrowsingScreen(
                navigateToRecipeDetails = { id -> navController.navigateToRecipeDetails(id) },
                onBackClick = navController::navigateUp,
            )
        }
    }

    composable(
        route = "${RecipesDestination.Details.route}/{$RECIPE_ID}",
        arguments = listOf(
            navArgument(RECIPE_ID) { type = NavType.StringType }
        )
    ) {
        RecipeDetailsScreen(
            onBackClick = navController::navigateUp,
            navigateToAuthenticateScreen = navigateToAuthenticateScreen,
        )
    }

    composable(
        route = "${RecipesDestination.Listing.route}/{$LISTING_TYPE}",
        arguments = listOf(
            navArgument(LISTING_TYPE) {
                type = NavType.StringType
            }
        )
    ) {
        RecipeListingScreen(
            navigateUp = navController::navigateUp,
            navigateToRecipeDetails = { id -> navController.navigateToRecipeDetails(id) }
        )
    }

    dialog("report") {
        RecipeReportDialog(
            onCloseDialog = navController::navigateUp,
        )
    }

    composable(
        route = "1234",
    ) {
        val viewModel: PepeViewModel = koinViewModel()
        val products = viewModel.products.collectAsLazyPagingItems()
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = navigateToAuthenticateScreen) {
                Text(text = products.itemCount.toString())
            }
        }
    }
}

private fun NavController.navigateToRecipeDetails(id: String) {
    navigate("${RecipesDestination.Details.route}/$id")
}

fun NavController.navigateToRecipeListing(listingType: String) {
    navigate("${RecipesDestination.Listing.route}/$listingType")
}

internal data class RecipeBrowsingNavArgs(
    val tag: RecipeTag? = null,
    val sorter: RecipeSorter? = null,
)

internal const val LISTING_TYPE = "listing-type"
internal const val RECIPE_ID = "recipe-id"


class PepeViewModel(
) : ViewModel() {
    private val searchChannel = Channel<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val products: Flow<PagingData<Int>> =
        searchChannel.receiveAsFlow().map {
            PagingData.from(listOf(2, 3))
//            val params = ProductQueryParams.Builder().build()
//            productRepository.queryProductsPagingDataFlow(params).cachedIn(viewModelScope)
//                .map { pagingData ->
//                    pagingData.map { model -> model.toView() }
//                }
        }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            searchChannel.send(Unit)
        }
    }
}
