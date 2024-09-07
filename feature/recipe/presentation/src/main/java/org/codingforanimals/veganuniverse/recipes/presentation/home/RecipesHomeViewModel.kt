package org.codingforanimals.veganuniverse.recipes.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.recipe.domain.repository.RecipeRepository
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.Recipe
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeQueryParams
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeSorter
import org.codingforanimals.veganuniverse.commons.recipe.shared.model.RecipeTag
import org.codingforanimals.veganuniverse.recipes.presentation.RecipeBrowsingNavArgs

internal class RecipesHomeViewModel(
    private val recipeRepository: RecipeRepository,
) : ViewModel() {

    private val sideEffectsChannel: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = sideEffectsChannel.receiveAsFlow()

    val mostLikedRecipes: StateFlow<RecipeListState> = flow {
        val params = RecipeQueryParams.Builder()
            .withMaxSize(10)
            .withPageSize(10)
            .withSorter(RecipeSorter.LIKES)
            .build()
        val result = runCatching {
            recipeRepository.queryRecipes(params)
        }.getOrNull()?.let { recipes ->
            RecipeListState.Success(recipes)
        } ?: RecipeListState.Error
        emit(result)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RecipeListState.Loading,
    )

    val latestRecipes: StateFlow<RecipeListState> = flow {
        val params = RecipeQueryParams.Builder()
            .withMaxSize(4)
            .withPageSize(4)
            .withSorter(RecipeSorter.DATE)
            .build()
        val result = runCatching {
            recipeRepository.queryRecipes(params)
        }.getOrNull()?.let { recipes ->
            RecipeListState.Success(recipes)
        } ?: RecipeListState.Error
        emit(result)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RecipeListState.Loading,
    )

    fun onAction(action: Action) {
        when (action) {
            is Action.OnRecipeTagClick -> {
                viewModelScope.launch {
                    val navArgs = RecipeBrowsingNavArgs(tag = action.recipeTag)
                    sideEffectsChannel.send(SideEffect.NavigateToRecipeBrowsing(navArgs))
                }
            }

            is Action.OnRecipeClick -> {
                val id = action.recipe.id ?: return
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateToRecipe(id))
                }
            }

            Action.OnShowLatestRecipesClick -> {
                viewModelScope.launch {
                    val navArgs = RecipeBrowsingNavArgs(sorter = RecipeSorter.DATE)
                    sideEffectsChannel.send(SideEffect.NavigateToRecipeBrowsing(navArgs))
                }
            }

            Action.OnShowMostLikedRecipesClick -> {
                viewModelScope.launch {
                    val navArgs = RecipeBrowsingNavArgs(sorter = RecipeSorter.LIKES)
                    sideEffectsChannel.send(SideEffect.NavigateToRecipeBrowsing(navArgs))
                }
            }

            Action.OnBackClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateUp)
                }
            }
        }
    }

    sealed class RecipeListState {
        data object Loading : RecipeListState()
        data object Error : RecipeListState()
        data class Success(val recipes: List<Recipe>) : RecipeListState()
    }

    sealed class Action {
        data object OnShowLatestRecipesClick : Action()
        data object OnShowMostLikedRecipesClick : Action()
        data object OnBackClick : Action()

        data class OnRecipeTagClick(val recipeTag: RecipeTag) : Action()
        data class OnRecipeClick(val recipe: Recipe) : Action()
    }

    sealed class SideEffect {
        data object NavigateUp : SideEffect()
        data class NavigateToRecipe(val id: String) : SideEffect()
        data class NavigateToRecipeBrowsing(val navArgs: RecipeBrowsingNavArgs) : SideEffect()
    }
}
