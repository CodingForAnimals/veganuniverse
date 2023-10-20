@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.recipes.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.ui.R.string.all_tags
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_03
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.RecipeBrowsingNavArgs
import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeViewModel.Action
import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeViewModel.RelayAction
import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeViewModel.SideEffect
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.RecipeCarousel
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.cardShapeAndShadow
import org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.RecipeTagContainer
import org.codingforanimals.veganuniverse.recipes.ui.RecipeTag
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RecipesHomeScreen(
    navigateToRecipeBrowsing: (RecipeBrowsingNavArgs) -> Unit,
    navigateToRecipe: (String) -> Unit,
    viewModel: RecipesHomeViewModel = koinViewModel(),
) {
    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateToRecipe = navigateToRecipe,
        navigateToRecipeBrowsing = navigateToRecipeBrowsing,
    )

    RecipesHomeScreen(
        content = viewModel.content,
        onAction = viewModel::onAction,
        onRelayAction = viewModel::onRelayAction,
    )
}

@Composable
private fun RecipesHomeScreen(
    content: List<RecipeHomeScreenItem>,
    onAction: (Action) -> Unit,
    onRelayAction: (RelayAction) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(Spacing_06),
        verticalArrangement = Arrangement.spacedBy(Spacing_06)
    ) {
        items(content) { item ->
            when (item) {
                is RecipeHomeScreenItem.Carousel -> RecipeCarousel(
                    sorter = item.sorter,
                    navigateToRecipe = { onRelayAction(RelayAction.OnRecipeCardClick(it)) },
                    navigateToRecipeBrowsing = { onRelayAction(RelayAction.OnShowAllClick(it)) },
                )

                is RecipeHomeScreenItem.Container -> RecipeTagContainer(
                    tag = item.tag,
                    layoutType = item.layoutType,
                    navigateToRecipe = { onRelayAction(RelayAction.OnRecipeCardClick(it)) },
                    navigateToRecipeBrowsing = { onRelayAction(RelayAction.OnShowAllClick(it)) },
                )
            }
        }
        item {
            AllRecipeTags(
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun AllRecipeTags(
    modifier: Modifier = Modifier,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        Text(
            text = stringResource(all_tags),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
        )
        FlowRow(
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.spacedBy(Spacing_04),
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
        ) {
            RecipeTag.values().forEachIndexed { index, recipeTag ->
                key(index) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .cardShapeAndShadow()
                            .clickable {
                                onAction(Action.OnRecipeTagCardClick(recipeTag))
                            },
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .weight(1f)
                                    .sizeIn(maxWidth = 50.dp, maxHeight = 50.dp)
                                    .padding(top = Spacing_06, bottom = Spacing_04),
                                model = recipeTag.icon.model,
                                contentScale = ContentScale.Fit,
                                contentDescription = stringResource(recipeTag.label),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .fillMaxWidth()
                                    .padding(Spacing_03),
                                text = stringResource(recipeTag.label),
                                maxLines = 1,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
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
                is SideEffect.NavigateToRecipeBrowsing -> {
                    navigateToRecipeBrowsing(sideEffect.navArgs)
                }

                is SideEffect.NavigateToRecipe -> {
                    navigateToRecipe(sideEffect.id)
                }
            }
        }.collect()
    }
}