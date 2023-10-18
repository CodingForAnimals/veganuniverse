package org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.carouselcard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.recipes.entity.Recipe
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.usecase.GetCarouselRecipesUseCase

private const val TAG = "RecipeCarouselCardViewM"

class RecipeCarouselAsyncCardsViewModel(
    deferred: Deferred<List<Recipe>>,
) : ViewModel() {

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    val recipes = flow {
        val state = try {
            val recipes = deferred.await()
            if (recipes.size.toLong() != GetCarouselRecipesUseCase.CAROUSEL_QUERY_LIMIT) {
                sideEffectsChannel.send(SideEffect.NoMoreRecipes)
            }
            RecipeState.Success(recipes)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            sideEffectsChannel.send(SideEffect.NoMoreRecipes)
            RecipeState.Error
        }
        emit(state)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RecipeState.Loading,
    )

    fun onAction(action: Action) {
        when (action) {
            is Action.OnRecipeClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateToRecipe(action.id))
                }
            }
        }
    }

    sealed class Action {
        data class OnRecipeClick(val id: String) : Action()
    }

    sealed class RecipeState {
        data object Loading : RecipeState()
        data object Error : RecipeState()
        data class Success(val recipes: List<Recipe>) : RecipeState()
    }

    sealed class SideEffect {
        data object NoMoreRecipes : SideEffect()
        data class NavigateToRecipe(val id: String) : SideEffect()
    }
}