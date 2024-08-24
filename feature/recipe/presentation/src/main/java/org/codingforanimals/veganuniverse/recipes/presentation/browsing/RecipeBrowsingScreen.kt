@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.recipes.presentation.browsing

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_08
import org.codingforanimals.veganuniverse.commons.recipe.presentation.toUI
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeSorter
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeTag
import org.codingforanimals.veganuniverse.commons.ui.R.drawable.ic_search
import org.codingforanimals.veganuniverse.commons.ui.components.SelectableChip
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.commons.ui.error.ErrorView
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.RecipeBrowsingViewModel.Action
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.RecipeBrowsingViewModel.SideEffect
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.RecipeBrowsingViewModel.UiState
import org.codingforanimals.veganuniverse.recipes.presentation.shared.RecipeCard
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RecipeBrowsingScreen(
    modifier: Modifier = Modifier,
    navigateToRecipeDetails: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: RecipeBrowsingViewModel = koinViewModel(),
) {

    val recipes = viewModel.recipes.collectAsLazyPagingItems()

    val focusManager = LocalFocusManager.current

    RecipeBrowsingScreen(
        modifier = modifier,
        uiState = viewModel.uiState,
        onAction = viewModel::onAction,
        recipes = recipes,
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateUp = onBackClick,
        navigateToRecipeDetails = navigateToRecipeDetails,
        clearFocus = { focusManager.clearFocus() },
    )
}

@Composable
private fun RecipeBrowsingScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (Action) -> Unit,
    recipes: LazyPagingItems<Recipe>,
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

    Scaffold(
        modifier = modifier,
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                VUTopAppBar(
                    onBackClick = { onAction(Action.OnBackClick) },
                )
                Row(
                    modifier = Modifier.padding(
                        bottom = Spacing_04,
                        start = Spacing_05,
                        end = Spacing_05,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(Spacing_04),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = uiState.searchText,
                        onValueChange = { onAction(Action.OnSearchTextChange(it)) },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = ic_search),
                                contentDescription = null,
                            )
                        },
                        shape = ShapeDefaults.Medium,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        placeholder = { Text(text = stringResource(id = R.string.search_recipe)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        )
                    )
                    IconButton(
                        onClick = ::openFiltersSheet,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary,
                        ),
                        content = {
                            VUIcon(
                                icon = VUIcons.Filter,
                                contentDescription = ""
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier.padding(paddingValues),
            targetState = recipes.itemCount == 0 && recipes.loadState.refresh !is LoadState.Loading,
            label = "products_empty_state_cross_fade",
        ) { isEmpty ->
            if (isEmpty) {
                ErrorView(message = R.string.no_recipes_found)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing_04),
                    contentPadding = PaddingValues(
                        top = Spacing_05,
                        start = Spacing_05,
                        end = Spacing_05,
                        bottom = Spacing_08,
                    )
                ) {
                    items(recipes.itemCount) { index ->
                        val recipe = recipes[index] ?: return@items
                        val id = recipe.id ?: return@items
                        key(id) {
                            RecipeCard(
                                recipe = recipe,
                                onClick = { onAction(Action.OnRecipeClick(recipe)) }
                            )
                        }
                    }
                    recipes.loadState.apply {
                        when {
                            refresh is LoadState.Loading -> item { CircularProgressIndicator() }
                            append is LoadState.Loading -> item { CircularProgressIndicator() }
                        }
                    }
                }
            }
        }
        if (bottomSheetVisible) {
            ModalBottomSheet(
                sheetState = modalBottomSheetState,
                onDismissRequest = { bottomSheetVisible = false }
            ) {
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
                            RecipeTag.entries.forEach {
                                key(it.name.hashCode()) {
                                    val tagUI = remember(it) { it.toUI() }
                                    SelectableChip(
                                        label = stringResource(tagUI.label),
                                        icon = tagUI.icon,
                                        selected = currentTag == it,
                                        onClick = {
                                            currentTag = if (currentTag == it) null else it
                                        },
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
                            RecipeSorter.entries.forEach {
                                key(it.name.hashCode()) {
                                    val sorterUI = remember(it) { it.toUI() }
                                    SelectableChip(
                                        label = stringResource(sorterUI.label),
                                        icon = sorterUI.icon,
                                        selected = currentSorter == it,
                                        onClick = { currentSorter = it },
                                    )
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing_04))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = Spacing_06)
                        ) {
                            TextButton(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    currentTag = null
                                    currentSorter = RecipeSorter.DATE
                                    onAction(Action.OnClearFiltersClick)
                                    dismissFiltersSheet()
                                }) {
                                Text(text = stringResource(R.string.clear_filters))
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
                                    dismissFiltersSheet()
                                }) {
                                Text(text = stringResource(R.string.apply_filters))
                            }
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
    navigateUp: () -> Unit,
    navigateToRecipeDetails: (String) -> Unit,
    clearFocus: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateToRecipeDetails -> navigateToRecipeDetails(sideEffect.recipeId)
                SideEffect.NavigateUp -> navigateUp()
                SideEffect.ClearFocus -> clearFocus()
            }
        }.collect()
    }
}
