package org.codingforanimals.veganuniverse.recipes.presentation.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.recipe.model.Recipe
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.home.components.RecipesHomeItemHeader
import org.codingforanimals.veganuniverse.ui.Spacing_02
import org.codingforanimals.veganuniverse.ui.Spacing_03
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_05
import org.codingforanimals.veganuniverse.ui.animation.ShimmerItem
import org.codingforanimals.veganuniverse.ui.animation.shimmer
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.error.ErrorView
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

@Composable
internal fun MostLikedRecipes(
    mostLikedRecipesState: RecipesHomeViewModel.RecipeListState,
    onShowMoreClick: () -> Unit,
    onRecipeClick: (Recipe) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing_02),
    ) {
        RecipesHomeItemHeader(
            label = stringResource(R.string.recipes_carousel_title_most_liked),
            buttonLabel = stringResource(R.string.show_more),
            onButtonClick = onShowMoreClick,
        )
        Crossfade(
            targetState = mostLikedRecipesState,
            label = "most_liked_recipes_cross_fade",
        ) { state ->
            when (state) {
                RecipesHomeViewModel.RecipeListState.Error -> {
                    ErrorView(message = R.string.oops_something_went_wrong)
                }

                RecipesHomeViewModel.RecipeListState.Loading -> {
                    MostLikedRecipesLoadingLazyRow()
                }

                is RecipesHomeViewModel.RecipeListState.Success -> {
                    MostLikedRecipesLazyRow(
                        state = state,
                        onRecipeClick = onRecipeClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun MostLikedRecipesLazyRow(
    modifier: Modifier = Modifier,
    state: RecipesHomeViewModel.RecipeListState.Success,
    onRecipeClick: (Recipe) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        items(state.recipes) { recipe ->
            recipe.id ?: return@items
            key(recipe.id) {
                MostLikedRecipeCard(
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe) },
                )
            }
        }
    }
}

@Composable
private fun MostLikedRecipeCard(
    recipe: Recipe,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .aspectRatio(0.7f),
        onClick = onClick,
    ) {
        Box {
            AsyncImage(
                model = recipe.imageUrl,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray),
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Text(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(
                                top = Spacing_05,
                                start = Spacing_05,
                                end = Spacing_05,
                            ),
                        text = recipe.title,
                        minLines = 2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Start,
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(
                                start = Spacing_05,
                                end = Spacing_05,
                                bottom = Spacing_03,
                            ),
                        horizontalArrangement = Arrangement.spacedBy(
                            Spacing_03
                        ),
                    ) {
                        VUIcon(
                            modifier = Modifier.size(16.dp),
                            icon = VUIcons.FavoriteFilled,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.outline,
                        )
                        Text(
                            text = "${recipe.likes}",
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MostLikedRecipesLoadingLazyRow(
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing_04)
    ) {
        items(4) {
            RecipeCarouselLoadingCard()
        }
    }
}

@Composable
private fun RecipeCarouselLoadingCard() {
    Box(
        modifier = Modifier
            .width(150.dp)
            .aspectRatio(0.7f)
            .clip(ShapeDefaults.Small)
            .shimmer(),
    ) {
        ShimmerItem(modifier = Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.White)
            ) {
                ShimmerItem(
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth()
                        .padding(
                            top = Spacing_05,
                            start = Spacing_05,
                            end = Spacing_05,
                        )
                        .clip(ShapeDefaults.Small),
                )
                Spacer(modifier = Modifier.height(Spacing_05))
                ShimmerItem(
                    modifier = Modifier
                        .align(Alignment.End)
                        .width(80.dp)
                        .height(30.dp)
                        .padding(
                            end = Spacing_05,
                            bottom = Spacing_05,
                        )
                        .clip(ShapeDefaults.Small),
                )
            }
        }
    }
}
