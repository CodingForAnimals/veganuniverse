package org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.ui.R.string.show_more
import org.codingforanimals.veganuniverse.core.ui.animation.ShimmerItem
import org.codingforanimals.veganuniverse.core.ui.animation.shimmer
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.core.ui.theme.VeganUniverseTheme
import org.codingforanimals.veganuniverse.recipes.presentation.RecipeBrowsingNavArgs
import org.codingforanimals.veganuniverse.recipes.presentation.home.components.RecipesHomeItemHeader
import org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.RecipeTagContainerViewModel.Action
import org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.RecipeTagContainerViewModel.SideEffect
import org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.usecase.GetContainerRecipesUseCase
import org.codingforanimals.veganuniverse.recipes.ui.RecipeTag
import org.codingforanimals.veganuniverse.shared.ui.grid.ContainerLayoutType
import org.codingforanimals.veganuniverse.shared.ui.grid.StaggeredItemsGrid
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun RecipeTagContainer(
    modifier: Modifier = Modifier,
    tag: RecipeTag,
    layoutType: ContainerLayoutType,
    navigateToRecipe: (String) -> Unit,
    navigateToRecipeBrowsing: (RecipeBrowsingNavArgs) -> Unit,
    viewModel: RecipeTagContainerViewModel = koinViewModel(
        key = tag.name,
        parameters = { parametersOf(tag) },
    ),
) {

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateToRecipe = navigateToRecipe,
        navigateToRecipeBrowsing = navigateToRecipeBrowsing,
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        RecipesHomeItemHeader(
            icon = tag.icon,
            label = stringResource(tag.label),
            buttonLabel = stringResource(show_more),
            onButtonClick = { viewModel.onAction(Action.OnShowMoreClick) },
        )

        when (val state = viewModel.recipes.collectAsState().value) {
            GetContainerRecipesUseCase.Status.Error -> Unit
            GetContainerRecipesUseCase.Status.Loading -> LoadingRecipeTagContainer()
            is GetContainerRecipesUseCase.Status.Success -> StaggeredItemsGrid(
                items = state.recipes,
                layoutType = layoutType,
                onClick = { viewModel.onAction(Action.OnRecipeClick(it)) },
            )
        }
    }
}

@Composable
private fun LoadingRecipeTagContainer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shimmer(),
        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            ShimmerItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(ShapeDefaults.Small)
            )
            ShimmerItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(ShapeDefaults.Small)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
        ) {
            ShimmerItem(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(ShapeDefaults.Small)
            )
        }
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateToRecipe: (String) -> Unit,
    navigateToRecipeBrowsing: (RecipeBrowsingNavArgs) -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateToRecipe -> navigateToRecipe(sideEffect.id)
                is SideEffect.NavigateToRecipeBrowsing -> navigateToRecipeBrowsing(
                    RecipeBrowsingNavArgs(tag = sideEffect.tag, sorter = sideEffect.sorter)
                )
            }
        }.collect()
    }
}

@Composable
@Preview
private fun PreviewLoadingRecipeTagContainer() {
    VeganUniverseTheme {
        Surface(modifier = Modifier.padding(Spacing_06)) {
            LoadingRecipeTagContainer()
        }
    }
}

