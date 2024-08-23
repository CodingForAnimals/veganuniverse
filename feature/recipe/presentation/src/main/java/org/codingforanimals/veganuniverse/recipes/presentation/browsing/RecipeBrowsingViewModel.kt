@file:OptIn(ExperimentalCoroutinesApi::class)

package org.codingforanimals.veganuniverse.recipes.presentation.browsing

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.recipe.domain.repository.RecipeRepository
import org.codingforanimals.veganuniverse.commons.recipe.presentation.toUI
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeQueryParams
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeSorter
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeTag
import org.codingforanimals.veganuniverse.commons.ui.dialog.Dialog
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.RecipesDestination.Browsing.Companion.SORTER
import org.codingforanimals.veganuniverse.recipes.presentation.RecipesDestination.Browsing.Companion.TAG

private const val FILE_TAG = "RecipeBrowsingViewModel"

internal class RecipeBrowsingViewModel(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository,
) : ViewModel() {
    private val tagNavArgument = savedStateHandle.get<String>(TAG)
    private val sorterNavArgument = savedStateHandle.get<String>(SORTER)

    private val searchTextDelayMs = 750L
    private var searchTextDelayJob: Job? = null

    private val sideEffectsChannel: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = sideEffectsChannel.receiveAsFlow()

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.init(tagNavArgument, sorterNavArgument))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val searchChannel: Channel<Unit> = Channel()
    val recipes: Flow<PagingData<Recipe>> =
        searchChannel.receiveAsFlow().flatMapLatest {
            val params = RecipeQueryParams.Builder()
                .withSorter(uiState.value.sorter)
                .withTag(uiState.value.filterTag)
                .withTitle(uiState.value.searchText.ifBlank { null })
                .build()
            recipeRepository.queryRecipesPagingData(params)
        }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            searchRecipes()
        }
    }

    fun onAction(action: Action) {
        when (action) {
            Action.OnBackClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateUp)
                }
            }

            is Action.OnRecipeClick -> {
                val id = action.recipe.id ?: return
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateToRecipeDetails(id))
                }
            }

            Action.OnClearFiltersClick -> {
                val defaultFilterTag = null
                val defaultSorter = RecipeSorter.DATE
                if (uiState.value.filtersChanged(defaultFilterTag, defaultSorter)) {
                    _uiState.value = uiState.value.copy(
                        recipes = emptyList(),
                        filterTag = defaultFilterTag,
                        sorter = defaultSorter,
                    )
                    viewModelScope.launch {
                        searchRecipes()
                    }
                }
            }

            is Action.OnApplyFilters -> {
                if (uiState.value.filtersChanged(action.tag, action.sorter)) {
                    _uiState.value = uiState.value.copy(
                        recipes = emptyList(),
                        filterTag = action.tag,
                        sorter = action.sorter,
                    )
                    viewModelScope.launch {
                        searchRecipes()
                    }
                }
            }

            is Action.OnSearchTextChange -> {
                _uiState.value = uiState.value.copy(searchText = action.text)
                searchTextDelayJob?.cancel()
                if (action.text.isEmpty() || action.text.length >= 3) {
                    searchTextDelayJob = viewModelScope.launch {
                        delay(searchTextDelayMs)
                        searchRecipes()
                    }
                }
            }
        }
    }

    private suspend fun searchRecipes() = searchChannel.send(Unit)

    data class UiState(
        val recipes: List<Recipe> = emptyList(),
        val searchText: String = "",
        val filterTag: RecipeTag? = defaultTag,
        val sorter: RecipeSorter = defaultSorter,
        val dialog: Dialog? = null,
        val canLoadMore: Boolean = true,
    ) {
        private val filterTagUI = filterTag?.toUI()
        fun filtersChanged(
            tag: RecipeTag?,
            sorter: RecipeSorter,
        ): Boolean {
            return (filterTag != tag || this.sorter != sorter)
        }

        @StringRes
        val topBarLabel: Int = filterTagUI?.label ?: R.string.all_recipes

        companion object {
            val defaultSorter = RecipeSorter.LIKES
            val defaultTag = null
            fun init(rawTag: String?, rawSorter: String?): UiState {
                val tag = if (rawTag != null && rawTag != "null") {
                    try {
                        RecipeTag.valueOf(rawTag)
                    } catch (e: Throwable) {
                        Log.e(FILE_TAG, e.stackTraceToString())
                        defaultTag
                    }
                } else {
                    defaultTag
                }

                val sorter = if (rawSorter != null && rawSorter != "null") {
                    try {
                        RecipeSorter.valueOf(rawSorter)
                    } catch (e: Throwable) {
                        Log.e(FILE_TAG, e.stackTraceToString())
                        defaultSorter
                    }
                } else {
                    defaultSorter
                }

                return UiState(
                    filterTag = tag,
                    sorter = sorter,
                )
            }
        }
    }

    sealed class Action {
        data object OnBackClick : Action()
        data class OnRecipeClick(val recipe: Recipe) : Action()
        data object OnClearFiltersClick : Action()
        data class OnApplyFilters(
            val tag: RecipeTag?,
            val sorter: RecipeSorter,
        ) : Action()

        data class OnSearchTextChange(val text: String) : Action()
    }

    sealed class SideEffect {
        data object NavigateUp : SideEffect()
        data class NavigateToRecipeDetails(val recipeId: String) : SideEffect()
    }
}