package org.codingforanimals.veganuniverse.create.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.navigation.DeeplinkNavigator
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.VerifiedOnlyUserAction

class CreateHomeViewModel(
    private val verifiedOnlyUserAction: VerifiedOnlyUserAction,
    private val deeplinkNavigator: DeeplinkNavigator,
) : ViewModel() {

    fun onAction(action: Action) {
        viewModelScope.launch {
            verifiedOnlyUserAction {
                when (action) {
                    Action.OnCreatePlaceClick -> DeepLink.CreatePlace
                    Action.OnCreateProductClick -> DeepLink.CreateProduct
                    Action.OnCreateRecipeClick -> DeepLink.CreateRecipe
                    Action.OnCreateAdditiveClick -> DeepLink.CreateAdditive
                }.let { deeplink ->
                    deeplinkNavigator.navigate(deeplink)
                }
            }
        }
    }

    sealed class Action {
        data object OnCreatePlaceClick : Action()
        data object OnCreateRecipeClick : Action()
        data object OnCreateProductClick : Action()
        data object OnCreateAdditiveClick : Action()
    }
}