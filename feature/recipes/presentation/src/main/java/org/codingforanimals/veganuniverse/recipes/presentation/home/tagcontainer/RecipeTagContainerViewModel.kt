package org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.usecase.GetContainerRecipesUseCase
import org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter
import org.codingforanimals.veganuniverse.recipes.ui.RecipeTag

internal class RecipeTagContainerViewModel(
    private val recipeTag: RecipeTag,
    getContainerRecipes: GetContainerRecipesUseCase,
) : ViewModel() {

    private val sideEffectsChannel: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = sideEffectsChannel.receiveAsFlow()

    val recipes = flow {
        emit(getContainerRecipes(recipeTag))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = GetContainerRecipesUseCase.Status.Loading,
    )

    fun onAction(action: Action) {
        when (action) {
            is Action.OnRecipeClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateToRecipe(action.id))
                }
            }

            Action.OnShowMoreClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(
                        SideEffect.NavigateToRecipeBrowsing(
                            tag = recipeTag.name,
                            sorter = RecipeSorter.LIKES.name,
                        )
                    )
                }
            }
        }
    }

    sealed class Action {
        data class OnRecipeClick(val id: String) : Action()
        data object OnShowMoreClick : Action()
    }

    sealed class SideEffect {
        data class NavigateToRecipe(val id: String) : SideEffect()
        data class NavigateToRecipeBrowsing(val tag: String, val sorter: String) : SideEffect()
    }
}