package org.codingforanimals.veganuniverse.recipes.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.recipes.presentation.RecipeBrowsingNavArgs
import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeViewModel.Action
import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeViewModel.SideEffect
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RecipesHomeScreen(
    navigateToRecipeBrowsing: (RecipeBrowsingNavArgs) -> Unit,
    navigateToRecipeDetails: (String) -> Unit,
) {
    val viewModel: RecipesHomeViewModel = koinViewModel()
    val mostLikedRecipesState by viewModel.mostLikedRecipes.collectAsStateWithLifecycle()
    val latestRecipesState by viewModel.latestRecipes.collectAsStateWithLifecycle()

    RecipesHomeScreens(
        mostLikedRecipesState = mostLikedRecipesState,
        latestRecipesState = latestRecipesState,
        onAction = viewModel::onAction,
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateToRecipeDetails = navigateToRecipeDetails,
        navigateToRecipeBrowsing = navigateToRecipeBrowsing,
    )
}

@Composable
private fun RecipesHomeScreens(
    mostLikedRecipesState: RecipesHomeViewModel.RecipeListState,
    latestRecipesState: RecipesHomeViewModel.RecipeListState,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(Spacing_06),
        verticalArrangement = Arrangement.spacedBy(Spacing_06)
    ) {
        item {
            MostLikedRecipes(
                mostLikedRecipesState = mostLikedRecipesState,
                onShowMoreClick = { onAction(Action.OnShowMostLikedRecipesClick) },
                onRecipeClick = { onAction(Action.OnRecipeClick(it)) },
            )
        }
        item {
            LatestRecipes(
                latestRecipesState = latestRecipesState,
                onShowMoreClick = { onAction(Action.OnShowLatestRecipesClick) },
                onRecipeClick = { onAction(Action.OnRecipeClick(it)) },
            )
        }
        item {
            AllRecipeTags(
                onRecipeTagClick = { onAction(Action.OnRecipeTagClick(it)) },
            )
        }
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateToRecipeDetails: (String) -> Unit,
    navigateToRecipeBrowsing: (RecipeBrowsingNavArgs) -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateToRecipeBrowsing -> {
                    navigateToRecipeBrowsing(sideEffect.navArgs)
                }

                is SideEffect.NavigateToRecipe -> {
                    navigateToRecipeDetails(sideEffect.id)
                }
            }
        }.collect()
    }
}
