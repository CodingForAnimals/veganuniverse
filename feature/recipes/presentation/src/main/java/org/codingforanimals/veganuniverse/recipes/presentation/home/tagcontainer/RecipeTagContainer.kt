package org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.ui.R.string.show_more
import org.codingforanimals.veganuniverse.core.ui.animation.ShimmerItem
import org.codingforanimals.veganuniverse.core.ui.animation.shimmer
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.core.ui.theme.VeganUniverseTheme
import org.codingforanimals.veganuniverse.recipes.presentation.category.RecipeBrowsingNavArgs
import org.codingforanimals.veganuniverse.recipes.presentation.home.components.RecipesHomeItemHeader
import org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.RecipeTagContainerViewModel.Action
import org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.RecipeTagContainerViewModel.SideEffect
import org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.usecase.GetContainerRecipesUseCase
import org.codingforanimals.veganuniverse.recipes.ui.RecipeTag
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun RecipeTagContainer(
    modifier: Modifier = Modifier,
    tag: RecipeTag,
    layoutType: RecipeTagLayoutType,
    navigateToRecipe: (String) -> Unit,
    navigateToRecipeBrowsing: (RecipeBrowsingNavArgs) -> Unit,
    viewModel: RecipeTagContainerViewModel = koinViewModel(parameters = { parametersOf(tag) }),
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
    }
    when (val state = viewModel.recipes.collectAsState().value) {
        GetContainerRecipesUseCase.Status.Error -> Unit
        GetContainerRecipesUseCase.Status.Loading -> LoadingRecipeTagContainer()
        is GetContainerRecipesUseCase.Status.Success -> RecipeTagContainer(
            state = state,
            layoutType = layoutType,
            onAction = viewModel::onAction,
        )
    }
}

@Composable
private fun RecipeTagContainer(
    state: GetContainerRecipesUseCase.Status.Success,
    layoutType: RecipeTagLayoutType,
    onAction: (Action) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            when (layoutType) {
                RecipeTagLayoutType.FULL_COLUMN_LEFT -> {
                    state.recipes.getOrNull(1)?.let { recipe ->
                        RecipeSquareCard(
                            title = recipe.title,
                            imageRef = recipe.imageRef,
                            onClick = { onAction(Action.OnRecipeClick(recipe.id)) }
                        )
                    }
                    state.recipes.getOrNull(2)?.let { recipe ->
                        RecipeSquareCard(
                            title = recipe.title,
                            imageRef = recipe.imageRef,
                            onClick = { onAction(Action.OnRecipeClick(recipe.id)) }
                        )
                    }
                }

                RecipeTagLayoutType.FULL_COLUMN_RIGHT -> {
                    state.recipes.getOrNull(0)?.let { recipe ->
                        RecipeFullColumnCard(
                            title = recipe.title,
                            imageRef = recipe.imageRef,
                            onClick = { onAction(Action.OnRecipeClick(recipe.id)) }
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            when (layoutType) {
                RecipeTagLayoutType.FULL_COLUMN_LEFT -> {
                    state.recipes.getOrNull(0)?.let { recipe ->
                        RecipeFullColumnCard(
                            title = recipe.title,
                            imageRef = recipe.imageRef,
                            onClick = { onAction(Action.OnRecipeClick(recipe.id)) }
                        )
                    }
                }

                RecipeTagLayoutType.FULL_COLUMN_RIGHT -> {
                    state.recipes.getOrNull(1)?.let { recipe ->
                        RecipeSquareCard(
                            title = recipe.title,
                            imageRef = recipe.imageRef,
                            onClick = { onAction(Action.OnRecipeClick(recipe.id)) }
                        )
                    }
                    state.recipes.getOrNull(2)?.let { recipe ->
                        RecipeSquareCard(
                            title = recipe.title,
                            imageRef = recipe.imageRef,
                            onClick = { onAction(Action.OnRecipeClick(recipe.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeSquareCard(
    title: String,
    imageRef: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = imageRef,
            modifier = Modifier
                .weight(1f)
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop,
            contentDescription = title
        )
        CardTitleText(title = title)
    }
}

@Composable
private fun RecipeFullColumnCard(
    title: String,
    imageRef: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = imageRef,
            modifier = Modifier.weight(1f),
            contentScale = ContentScale.Crop,
            contentDescription = title
        )
        CardTitleText(title = title)
    }
}

@Composable
private fun CardTitleText(title: String) {
    Text(
        modifier = Modifier
            .wrapContentHeight()
            .heightIn(min = 50.dp)
            .fillMaxWidth()
            .padding(vertical = Spacing_04),
        text = title,
        maxLines = 2,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
    )
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
                    RecipeBrowsingNavArgs(tag = sideEffect.tag)
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

