package org.codingforanimals.veganuniverse.recipes.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.recipes.presentation.RecipeBrowsingNavArgs
import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeViewModel.Action
import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeViewModel.SideEffect
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.ui.topbar.HomeScreenTopAppBar
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RecipesHomeScreen(
    navigateUp: () -> Unit,
    navigateToRecipeBrowsing: (RecipeBrowsingNavArgs) -> Unit,
    navigateToRecipeDetails: (String) -> Unit,
) {
    val viewModel: RecipesHomeViewModel = koinViewModel()
    val mostLikedRecipesState by viewModel.mostLikedRecipes.collectAsStateWithLifecycle()
    val latestRecipesState by viewModel.latestRecipes.collectAsStateWithLifecycle()

    RecipesHomeScreen(
        mostLikedRecipesState = mostLikedRecipesState,
        latestRecipesState = latestRecipesState,
        onAction = viewModel::onAction,
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateToRecipeDetails = navigateToRecipeDetails,
        navigateToRecipeBrowsing = navigateToRecipeBrowsing,
        navigateUp = navigateUp,
    )
}

@Composable
private fun RecipesHomeScreen(
    mostLikedRecipesState: RecipesHomeViewModel.RecipeListState,
    latestRecipesState: RecipesHomeViewModel.RecipeListState,
    onAction: (Action) -> Unit,
) {
    Scaffold(
        topBar = {
            HomeScreenTopAppBar(
                title = stringResource(R.string.recipes_home_title),
                onBackClick = { onAction(Action.OnBackClick) },
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                bottom = Spacing_06,
                start = Spacing_05,
                end = Spacing_05,
            ),
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
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateToRecipeDetails: (String) -> Unit,
    navigateToRecipeBrowsing: (RecipeBrowsingNavArgs) -> Unit,
    navigateUp: () -> Unit,
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

                SideEffect.NavigateUp -> navigateUp()
            }
        }.collect()
    }
}
