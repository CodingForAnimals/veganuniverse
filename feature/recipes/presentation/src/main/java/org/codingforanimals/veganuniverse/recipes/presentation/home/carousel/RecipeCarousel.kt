package org.codingforanimals.veganuniverse.recipes.presentation.home.carousel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.ui.R.string.load_more
import org.codingforanimals.veganuniverse.core.ui.R.string.show_more
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.category.RecipeBrowsingNavArgs
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.RecipeCarouselViewModel.Action
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.RecipeCarouselViewModel.SideEffect
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.RecipeCarouselViewModel.UiState
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.carouselcard.RecipeCarouselAsyncCards
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.carouselcard.RecipeCarouselLoadingCard
import org.codingforanimals.veganuniverse.recipes.presentation.home.components.RecipesHomeItemHeader
import org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter
import org.codingforanimals.veganuniverse.utils.toDp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun RecipeCarousel(
    modifier: Modifier = Modifier,
    sorter: RecipeSorter,
    navigateToRecipe: (String) -> Unit,
    navigateToRecipeBrowsing: (RecipeBrowsingNavArgs) -> Unit,
    viewModel: RecipeCarouselViewModel = koinViewModel(parameters = { parametersOf(sorter) }),
) {
    val uiState = viewModel.uiState

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateToRecipe = navigateToRecipe,
        navigateToRecipeBrowsing = navigateToRecipeBrowsing,
    )

    RecipeCarousel(
        modifier = modifier,
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun RecipeCarousel(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        RecipesHomeItemHeader(
            icon = VUIcons.Favorite,
            label = stringResource(R.string.recipes_carousel_title_most_liked),
            buttonLabel = stringResource(show_more),
            onButtonClick = { onAction(Action.OnShowMoreClick) }
        )
        LazyRow(
            modifier = Modifier.heightIn(min = (cardWidth() * 1.42).toDp()),
            horizontalArrangement = Arrangement.spacedBy(Spacing_04)
        ) {
            if (uiState.recipes.isEmpty()) {
                items(3) {
                    RecipeCarouselLoadingCard()
                }
            } else {
                items(uiState.recipes) {
                    key(it.hashCode()) {
                        RecipeCarouselAsyncCards(
                            recipes = it,
                            hideLoadMoreButton = { onAction(Action.HideLoadMoreButton) },
                            navigateToRecipe = { onAction(Action.OnRecipeClick(it)) }
                        )
                    }
                }
            }
            item {
                LoadMoreButton(
                    visible = !uiState.loading && uiState.canLoadMore,
                    onLoadMoreClick = { onAction(Action.OnLoadMoreClick) },
                )
            }
        }
    }
}

@Composable
private fun LoadMoreButton(
    visible: Boolean,
    onLoadMoreClick: () -> Unit,
) {
    AnimatedVisibility(visible = visible) {
        TextButton(
            modifier = Modifier
                .width(cardWidth().toDp())
                .aspectRatio(0.7f),
            shape = ShapeDefaults.Small,
            onClick = onLoadMoreClick,
        ) {
            Text(stringResource(load_more))
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
                is SideEffect.NavigateToRecipe -> {
                    navigateToRecipe(sideEffect.id)
                }

                is SideEffect.NavigateToRecipeBrowsing -> {
                    navigateToRecipeBrowsing(
                        RecipeBrowsingNavArgs(
                            sorter = sideEffect.sorter
                        )
                    )
                }
            }
        }.collect()
    }
}