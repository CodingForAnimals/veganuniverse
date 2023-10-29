package org.codingforanimals.veganuniverse.recipes.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.recipes.presentation.RecipeBrowsingNavArgs
import org.codingforanimals.veganuniverse.recipes.ui.RecipeSorter
import org.codingforanimals.veganuniverse.recipes.ui.RecipeTag
import org.codingforanimals.veganuniverse.shared.ui.grid.ContainerLayoutType

internal class RecipesHomeViewModel : ViewModel() {

    private val sideEffectsChannel: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = sideEffectsChannel.receiveAsFlow()

    val content = listOf(
        RecipeHomeScreenItem.Carousel(
            sorter = RecipeSorter.LIKES
        ),
        RecipeHomeScreenItem.Container(
            tag = RecipeTag.BREAKFAST_AND_EVENING,
            layoutType = ContainerLayoutType.VERTICAL_RIGHT
        ),
        RecipeHomeScreenItem.Container(
            tag = RecipeTag.QUICK_RECIPE,
            layoutType = ContainerLayoutType.VERTICAL_LEFT
        ),
        RecipeHomeScreenItem.Container(
            tag = RecipeTag.LUNCH_AND_DINNER,
            layoutType = ContainerLayoutType.VERTICAL_RIGHT
        ),
    )

    fun onAction(action: Action) {
        when (action) {
            is Action.OnRecipeTagCardClick -> {
                viewModelScope.launch {
                    val navArgs = RecipeBrowsingNavArgs(tag = action.recipeTag.name)
                    sideEffectsChannel.send(SideEffect.NavigateToRecipeBrowsing(navArgs))
                }
            }
        }
    }

    fun onRelayAction(action: RelayAction) {
        when (action) {
            is RelayAction.OnRecipeCardClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateToRecipe(action.id))
                }
            }

            is RelayAction.OnShowAllClick -> {
                viewModelScope.launch {
                    sideEffectsChannel.send(SideEffect.NavigateToRecipeBrowsing(action.navArgs))
                }
            }
        }
    }

    sealed class Action {
        data class OnRecipeTagCardClick(val recipeTag: RecipeTag) : Action()
    }

    sealed class RelayAction {
        data class OnRecipeCardClick(val id: String) : RelayAction()
        data class OnShowAllClick(val navArgs: RecipeBrowsingNavArgs) : RelayAction()
    }

    sealed class SideEffect {
        data class NavigateToRecipe(val id: String) : SideEffect()
        data class NavigateToRecipeBrowsing(val navArgs: RecipeBrowsingNavArgs) : SideEffect()
    }
}

internal sealed class RecipeHomeScreenItem {
    data class Carousel(val sorter: RecipeSorter) : RecipeHomeScreenItem()
    data class Container(val tag: RecipeTag, val layoutType: ContainerLayoutType) :
        RecipeHomeScreenItem()
}