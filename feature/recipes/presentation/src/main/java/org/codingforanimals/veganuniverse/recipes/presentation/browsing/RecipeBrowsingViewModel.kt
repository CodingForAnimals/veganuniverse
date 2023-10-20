package org.codingforanimals.veganuniverse.recipes.presentation.browsing

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.core.ui.model.Dialog
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.presentation.R
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.model.GetRecipesStatus
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.usecase.GetRecipesUseCase
import org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter
import org.codingforanimals.veganuniverse.recipes.ui.RecipeTag

private const val TAG = "RecipeBrowsingViewModel"

internal class RecipeBrowsingViewModel(
    savedStateHandle: SavedStateHandle,
    private val getRecipesUseCase: GetRecipesUseCase,
) : ViewModel() {

    private var getRecipesJob: Job? = null

    private val tagNavArgument = savedStateHandle.get<String>("tag")
    private val sorterNavArgument = savedStateHandle.get<String>("sorter")

    private val sideEffectsChannel: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = sideEffectsChannel.receiveAsFlow()

    var uiState by mutableStateOf(UiState.init(tagNavArgument, sorterNavArgument))

    init {
        getRecipes()
    }

    fun onAction(action: Action) {
        when (action) {
            Action.LoadMoreClick -> getRecipes()
            Action.OnBackClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateUp)
                }
            }

            is Action.OnCardClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateToRecipeDetails(action.recipeId))
                }
            }

            Action.OnFilterIconClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.OpenFiltersBottomSheet)
                }
            }

            Action.DismissFiltersSheet -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.DismissFiltersSheet)
                }
            }

            Action.OnClearFiltersClick -> {
                val defaultFilterTag = null
                val defaultSorter = RecipeSorter.DATE
                if (uiState.filtersChanged(defaultFilterTag, defaultSorter)) {
                    uiState = uiState.copy(
                        recipes = emptyList(),
                        filterTag = defaultFilterTag,
                        sorter = defaultSorter,
                    )
                    getRecipes()
                }
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.DismissFiltersSheet)
                }
            }

            is Action.OnApplyFilters -> {
                if (uiState.filtersChanged(action.tag, action.sorter)) {
                    uiState = uiState.copy(
                        recipes = emptyList(),
                        filterTag = action.tag,
                        sorter = action.sorter,
                    )
                    getRecipes()
                }
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.DismissFiltersSheet)
                }
            }
        }
    }

    private fun getRecipes() {
        getRecipesJob?.cancel()
        getRecipesJob = viewModelScope.launch {
            getRecipesUseCase(
                uiState.recipes,
                uiState.filterTag,
                uiState.sorter,
            ).collectLatest { status ->
                when (status) {
                    GetRecipesStatus.Error -> {
                        uiState = uiState.copy(
                            loadingMore = false,
                            dialog = Dialog.unknownErrorDialog()
                        )
                    }

                    GetRecipesStatus.Loading -> {
                        uiState = uiState.copy(loadingMore = true)
                    }

                    is GetRecipesStatus.Success -> {
                        uiState = uiState.appendRecipes(status.content)
                    }
                }
            }
        }
    }

    data class UiState(
        val recipes: List<Recipe> = emptyList(),
        val filterTag: RecipeTag? = defaultTag,
        val sorter: RecipeSorter = defaultSorter,
        val dialog: Dialog? = null,
        val loadingMore: Boolean = false,
        val canLoadMore: Boolean = true,
    ) {
        fun filtersChanged(tag: RecipeTag?, sorter: RecipeSorter): Boolean {
            return (filterTag != tag || this.sorter != sorter)
        }

        @StringRes
        val topBarLabel: Int = filterTag?.label ?: R.string.all_recipes

        fun appendRecipes(newRecipes: List<Recipe>): UiState {
            val current = recipes.toMutableList()
            current.addAll(newRecipes)
            return copy(
                recipes = current,
                loadingMore = false,
                canLoadMore = newRecipes.isNotEmpty(),
            )
        }

        companion object {
            val defaultSorter = RecipeSorter.LIKES
            val defaultTag = null
            fun init(rawTag: String?, rawSorter: String?): UiState {
                val tag = rawTag?.let {
                    try {
                        RecipeTag.valueOf(it)
                    } catch (e: Throwable) {
                        Log.e(TAG, e.stackTraceToString())
                        defaultTag
                    }
                }

                val sorter = rawSorter?.let {
                    try {
                        RecipeSorter.valueOf(rawSorter)
                    } catch (e: Throwable) {
                        Log.e(TAG, e.stackTraceToString())
                        defaultSorter
                    }
                } ?: defaultSorter

                return UiState(
                    filterTag = tag,
                    sorter = sorter,
                )
            }
        }
    }

    sealed class Action {
        data object OnBackClick : Action()
        data object LoadMoreClick : Action()
        data class OnCardClick(val recipeId: String) : Action()
        data object OnFilterIconClick : Action()
        data object DismissFiltersSheet : Action()
        data object OnClearFiltersClick : Action()
        data class OnApplyFilters(val tag: RecipeTag?, val sorter: RecipeSorter) : Action()
    }

    sealed class SideEffect {
        data object NavigateUp : SideEffect()
        data class NavigateToRecipeDetails(val recipeId: String) : SideEffect()
        data object OpenFiltersBottomSheet : SideEffect()
        data object DismissFiltersSheet : SideEffect()
    }
}