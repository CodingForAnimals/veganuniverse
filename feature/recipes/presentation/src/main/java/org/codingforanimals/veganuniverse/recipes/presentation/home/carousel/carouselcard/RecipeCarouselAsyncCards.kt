package org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.carouselcard

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.ui.R.drawable.img_cat_error
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_02
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_03
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_05
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.cardShapeAndShadow
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.cardWidth
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.carouselcard.RecipeCarouselAsyncCardsViewModel.Action
import org.codingforanimals.veganuniverse.utils.toDp
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun RecipeCarouselAsyncCards(
    modifier: Modifier = Modifier,
    recipes: Deferred<List<Recipe>>,
    hideLoadMoreButton: () -> Unit,
    navigateToRecipe: (String) -> Unit,
    viewModel: RecipeCarouselAsyncCardsViewModel = koinViewModel(
        key = recipes.hashCode().toString(),
        parameters = { parametersOf(recipes) },
    ),
) {
    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        hideLoadMoreButton = hideLoadMoreButton,
        navigateToRecipe = navigateToRecipe,
    )

    RecipeCarouselAsyncCards(
        modifier = modifier,
        state = viewModel.recipes.collectAsState(),
        onAction = viewModel::onAction,
    )
}

@Composable
private fun RecipeCarouselAsyncCards(
    modifier: Modifier = Modifier,
    state: State<RecipeCarouselAsyncCardsViewModel.RecipeState>,
    onAction: (Action) -> Unit,
) {
    Crossfade(targetState = state.value, label = "") { currentState ->
        when (currentState) {
            RecipeCarouselAsyncCardsViewModel.RecipeState.Error -> {
                Column(
                    modifier = modifier
                        .width(cardWidth().toDp())
                        .aspectRatio(0.7f)
                        .clip(ShapeDefaults.Small),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AsyncImage(
                        modifier = Modifier.weight(1f),
                        model = img_cat_error, contentDescription = "Error loading recipes"
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing_02),
                        text = "¡Gatito, no de nuevo! Ha ocurrido un error, ¡Lo sentimos!",
                        textAlign = TextAlign.Center,
                    )
                }
            }

            RecipeCarouselAsyncCardsViewModel.RecipeState.Loading -> {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing_04)) {
                    RecipeCarouselLoadingCard()
                    RecipeCarouselLoadingCard()
                    RecipeCarouselLoadingCard()
                }
            }

            is RecipeCarouselAsyncCardsViewModel.RecipeState.Success -> {
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing_04)) {
                    currentState.recipes.forEach { recipe ->
                        key(recipe.id) {
                            Box(
                                modifier = modifier
                                    .width(cardWidth().toDp())
                                    .aspectRatio(0.7f)
                                    .cardShapeAndShadow()
                                    .clickable { onAction(Action.OnRecipeClick(recipe.id)) },
                            ) {
                                AsyncImage(
                                    model = recipe.imageRef,
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
                                            horizontalArrangement = Arrangement.spacedBy(Spacing_03),
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
                }
            }
        }
    }
}

@Composable
fun HandleSideEffects(
    sideEffects: Flow<RecipeCarouselAsyncCardsViewModel.SideEffect>,
    hideLoadMoreButton: () -> Unit,
    navigateToRecipe: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                RecipeCarouselAsyncCardsViewModel.SideEffect.NoMoreRecipes -> {
                    hideLoadMoreButton()
                }

                is RecipeCarouselAsyncCardsViewModel.SideEffect.NavigateToRecipe -> {
                    navigateToRecipe(sideEffect.id)
                }
            }
        }.collect()
    }
}