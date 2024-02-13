@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)

package org.codingforanimals.veganuniverse.recipes.presentation.browsing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.core.ui.R.string.load_more
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.RecipeBrowsingViewModel.Action
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.RecipeBrowsingViewModel.SideEffect
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.RecipeBrowsingViewModel.UiState
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.components.RecipeCard
import org.codingforanimals.veganuniverse.ui.Spacing_02
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_05
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.VeganUniverseTheme
import org.codingforanimals.veganuniverse.ui.animation.ShimmerItem
import org.codingforanimals.veganuniverse.ui.animation.animateAlphaOnStart
import org.codingforanimals.veganuniverse.ui.animation.shimmer
import org.codingforanimals.veganuniverse.ui.components.SelectableChip
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.utils.TimeAgo
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RecipeBrowsingScreen(
    navigateToRecipeDetails: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: RecipeBrowsingViewModel = koinViewModel(),
) {

    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var bottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    fun openFiltersSheet() {
        bottomSheetVisible = true
    }

    fun dismissFiltersSheet() {
        coroutineScope.launch { modalBottomSheetState.hide() }.invokeOnCompletion {
            if (!modalBottomSheetState.isVisible) {
                bottomSheetVisible = false
            }
        }
    }

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateUp = onBackClick,
        navigateToRecipeDetails = navigateToRecipeDetails,
        openFiltersSheet = ::openFiltersSheet,
        dismissFilterSheet = ::dismissFiltersSheet,
    )

    RecipeBrowsingScreen(
        modalBottomSheetState = modalBottomSheetState,
        bottomSheetVisible = bottomSheetVisible,
        uiState = viewModel.uiState,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun RecipeBrowsingScreen(
    modalBottomSheetState: SheetState = rememberModalBottomSheetState(),
    bottomSheetVisible: Boolean = false,
    uiState: UiState,
    onAction: (Action) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        VUTopAppBar(
            title = stringResource(uiState.topBarLabel),
            onBackClick = { onAction(Action.OnBackClick) },
            actions = {
                IconButton(
                    onClick = { onAction(Action.OnFilterIconClick) },
                    content = {
                        VUIcon(
                            icon = VUIcons.Filter,
                            contentDescription = ""
                        )
                    }
                )
            },
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing_04),
            contentPadding = PaddingValues(vertical = Spacing_05, horizontal = Spacing_06)
        ) {
            items(uiState.recipes) { recipe ->
                key(recipe.id) {
                    RecipeCard(
                        modifier = Modifier.animateAlphaOnStart(),
                        onCardClick = { onAction(Action.OnCardClick(recipe.id)) },
                        userProfileThumbnail = "",
                        title = recipe.title,
                        username = recipe.username,
                        createdTimeAgo = TimeAgo.getTimeAgo(recipe.createdAt.time),
                        onReportClick = {},
                        description = recipe.description,
                        recipeImageUrl = recipe.imageRef,
                        onLikeClick = {},
                        likesCount = recipe.likes,
                    )
                }
            }
            item {
                AnimatedVisibility(
                    visible = uiState.loadingMore,
                ) {
                    LoadingRecipe()
                    LoadingRecipe()
                }
            }
            item {
                AnimatedVisibility(
                    visible = uiState.canLoadMore && !uiState.loadingMore,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    TextButton(onClick = { onAction(Action.LoadMoreClick) }) {
                        Text(text = stringResource(load_more))
                    }
                }
            }
        }
    }
    if (bottomSheetVisible) {
        ModalBottomSheet(
            sheetState = modalBottomSheetState,
            onDismissRequest = { onAction(Action.DismissFiltersSheet) }) {
            var currentTag by rememberSaveable { mutableStateOf(uiState.filterTag) }
            var currentSorter by rememberSaveable { mutableStateOf(uiState.sorter) }
            LazyColumn(
                modifier = Modifier.padding(Spacing_06),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing_04),
            ) {
                item {
                    Text(
                        text = stringResource(R.string.filter_by),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalArrangement = Arrangement.spacedBy(Spacing_02),
                    ) {
                        org.codingforanimals.veganuniverse.recipes.ui.RecipeTag.values().forEach {
                            key(it.name.hashCode()) {
                                SelectableChip(
                                    label = stringResource(it.label),
                                    icon = it.icon,
                                    selected = currentTag == it,
                                    onClick = { currentTag = if (currentTag == it) null else it },
                                )
                            }
                        }
                    }
                    Text(
                        text = stringResource(R.string.sort_by),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalArrangement = Arrangement.spacedBy(Spacing_02),
                    ) {
                        org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter.values()
                            .forEach {
                                key(it.name.hashCode()) {
                                    SelectableChip(
                                        label = stringResource(it.label),
                                        icon = it.icon,
                                        selected = currentSorter == it,
                                        onClick = { currentSorter = it },
                                    )
                                }
                            }
                    }
                    Divider(modifier = Modifier.padding(vertical = Spacing_04))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Spacing_06)
                    ) {
                        TextButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                currentTag = null
                                currentSorter =
                                    org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter.DATE
                                onAction(Action.OnClearFiltersClick)
                            }) {
                            Text(text = "Limpiar filtros")
                        }
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onAction(
                                    Action.OnApplyFilters(
                                        currentTag,
                                        currentSorter
                                    )
                                )
                            }) {
                            Text(text = "Aplicar filtros")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingRecipe() {
    Card(
        modifier = Modifier
            .clip(ShapeDefaults.Small)
            .shimmer()
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = Spacing_04,
                vertical = Spacing_04
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing_04)
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(Spacing_04),
            ) {
                ShimmerItem(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape),
                )
                Column(verticalArrangement = Arrangement.spacedBy(Spacing_02)) {
                    ShimmerItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .clip(ShapeDefaults.Small)
                    )
                    ShimmerItem(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(20.dp)
                            .clip(ShapeDefaults.Small)
                    )
                }
            }
            ShimmerItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .clip(ShapeDefaults.Small),
            )
            ShimmerItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .clip(ShapeDefaults.Small),
            )
            ShimmerItem(
                modifier = Modifier
                    .height(ButtonDefaults.MinHeight)
                    .clip(ShapeDefaults.Small)
                    .width(ButtonDefaults.MinWidth)
            )
        }
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateUp: () -> Unit,
    navigateToRecipeDetails: (String) -> Unit,
    openFiltersSheet: () -> Unit,
    dismissFilterSheet: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateToRecipeDetails -> navigateToRecipeDetails(sideEffect.recipeId)
                SideEffect.NavigateUp -> navigateUp()
                SideEffect.OpenFiltersBottomSheet -> openFiltersSheet()
                SideEffect.DismissFiltersSheet -> dismissFilterSheet()
            }
        }.collect()
    }
}

private val recipes = listOf(
    Recipe(
        id = "",
        userId = "",
        username = "El Pepe Argento",
        title = "Pastel de papa",
        description = "Muy rico y facil pastel de papa",
        likes = 12,
        createdAt = Date(),
        tags = emptyList(),
        ingredients = listOf("500g de papa", "200g de soja texturizada"),
        steps = listOf("Cocinar la papa", "Mezclar con la soja"),
        prepTime = "1 hora y media",
        servings = "8 porciones",
        imageRef = ""
    ),
    Recipe(
        id = "",
        userId = "",
        username = "Sucutrule",
        title = "Empanada de lenteja",
        description = "Excelente livianita empanada de lentejas",
        likes = 1,
        createdAt = Date(),
        tags = emptyList(),
        ingredients = listOf("500g de lentejas", "12 tapas de empanadas", "50g de aceitunas"),
        steps = listOf("Hervir las lentejas", "Picar las aceitunas", "Armar las empanadas"),
        prepTime = "1 hora",
        servings = "12 empanadas",
        imageRef = "",
    ),
)

@Preview
@Composable
private fun PreviewRecipeCardsScreen() {
    VeganUniverseTheme {
        Surface {
            RecipeBrowsingScreen(uiState = UiState(recipes = recipes), onAction = {})
        }
    }
}

@Preview
@Composable
private fun PreviewLoadingRecipeCardsScreen() {
    VeganUniverseTheme {
        Surface {
            RecipeBrowsingScreen(uiState = UiState(loadingMore = true), onAction = {})
        }
    }
}
