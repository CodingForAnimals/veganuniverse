@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.recipes.presentation.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.home.components.RecipesHomeItemHeader
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.ui.animation.ShimmerItem
import org.codingforanimals.veganuniverse.commons.ui.animation.shimmer
import org.codingforanimals.veganuniverse.commons.ui.error.ErrorView

@Composable
internal fun LatestRecipes(
    latestRecipesState: RecipesHomeViewModel.RecipeListState,
    onShowMoreClick: () -> Unit,
    onRecipeClick: (Recipe) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        RecipesHomeItemHeader(
            label = stringResource(R.string.latest_recipes),
            buttonLabel = stringResource(R.string.show_more),
            onButtonClick = onShowMoreClick
        )
        Crossfade(
            targetState = latestRecipesState,
            label = "latest_recipes_cross_fade",
        ) { state ->
            when (state) {
                RecipesHomeViewModel.RecipeListState.Error -> {
                    ErrorView(message = R.string.oops_something_went_wrong)
                }

                RecipesHomeViewModel.RecipeListState.Loading -> {
                    LatestRecipesLoadingFlowRow()
                }

                is RecipesHomeViewModel.RecipeListState.Success -> {
                    LatestRecipesFlowRow(
                        state = state,
                        onRecipeClick = { onRecipeClick(it) },
                    )
                }
            }
        }
    }
}

@Composable
private fun LatestRecipesFlowRow(
    modifier: Modifier = Modifier,
    state: RecipesHomeViewModel.RecipeListState.Success,
    onRecipeClick: (Recipe) -> Unit,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        maxItemsInEachRow = 2,
        horizontalArrangement = Arrangement.spacedBy(Spacing_04),
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        state.recipes.forEach { recipe ->
            recipe.id ?: return@forEach
            key(recipe.id) {
                Card(
                    modifier = Modifier.weight(1f),
                    onClick = { onRecipeClick(recipe) },
                    shape = ShapeDefaults.Medium,
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        model = recipe.imageUrl,
                        contentDescription = null,
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing_04),
                        text = recipe.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun LatestRecipesLoadingFlowRow(
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        maxItemsInEachRow = 2,
        horizontalArrangement = Arrangement.spacedBy(Spacing_04),
        verticalArrangement = Arrangement.spacedBy(Spacing_04),
    ) {
        for (i in 1..4) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(ShapeDefaults.Medium)
                    .shimmer(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ShimmerItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    ShimmerItem(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(40.dp)
                            .padding(vertical = Spacing_04)
                            .clip(ShapeDefaults.Medium)
                    )
                }
            }
        }
    }
}
