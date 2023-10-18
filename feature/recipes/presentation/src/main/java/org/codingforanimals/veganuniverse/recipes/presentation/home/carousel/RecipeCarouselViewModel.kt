package org.codingforanimals.veganuniverse.recipes.presentation.home.carousel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.usecase.GetCarouselRecipesUseCase
import org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter

internal class RecipeCarouselViewModel(
    private val sorter: RecipeSorter,
    private val getCarouselRecipesUseCase: GetCarouselRecipesUseCase,
) : ViewModel() {

    private var getCarouselRecipesJob: Job? = null

    private val sideEffectsChannel: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = sideEffectsChannel.receiveAsFlow()

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getCarouselRecipes()
    }

    private fun getCarouselRecipes() {
        getCarouselRecipesJob?.cancel()
        getCarouselRecipesJob = viewModelScope.launch {
            val deferredRecipes = getCarouselRecipesUseCase(
                recipes = uiState.recipes.lastOrNull()?.await() ?: emptyList(),
                sorter = sorter,
            )
            uiState = uiState.appendRecipes(deferredRecipes)
        }
    }

    fun onAction(action: Action) {
        when (action) {
            Action.OnLoadMoreClick -> getCarouselRecipes()
            Action.HideLoadMoreButton -> uiState = uiState.copy(
                canLoadMore = false,
            )

            is Action.OnRecipeClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateToRecipe(action.id))
                }
            }

            is Action.OnShowMoreClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateToRecipeBrowsing(sorter = sorter.name))
                }
            }
        }
    }

    data class UiState(
        val recipes: List<Deferred<List<Recipe>>> = emptyList(),
        val loading: Boolean = true,
        val canLoadMore: Boolean = true,
    ) {
        fun appendRecipes(newRecipes: Deferred<List<Recipe>>): UiState {
            val current = recipes.toMutableList()
            current.add(newRecipes)
            return copy(
                recipes = current,
                loading = false,
                canLoadMore = recipes.size < 4,
            )
        }
    }

    sealed class Action {
        data object OnLoadMoreClick : Action()
        data object HideLoadMoreButton : Action()
        data class OnRecipeClick(val id: String) : Action()
        data object OnShowMoreClick : Action()
    }

    sealed class SideEffect {
        data class NavigateToRecipe(val id: String) : SideEffect()
        data class NavigateToRecipeBrowsing(val sorter: String?) : SideEffect()
    }
}