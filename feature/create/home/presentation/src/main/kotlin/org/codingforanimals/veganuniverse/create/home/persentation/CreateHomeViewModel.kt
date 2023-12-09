package org.codingforanimals.veganuniverse.create.home.persentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.auth.usecase.GetUserStatus

class CreateHomeViewModel(
    private val getUserStatus: GetUserStatus,
) : ViewModel() {

    private val sideEffectsChannel = Channel<SideEffect>()
    val sideEffects = sideEffectsChannel.receiveAsFlow()

    fun onAction(action: Action) {
        when (action) {
            Action.OnCreatePlaceClick -> {
                viewModelScope.launch {
                    val effect = if (isUserAuthenticated()) {
                        SideEffect.NavigateToCreatePlace
                    } else {
                        SideEffect.NavigateToAuthenticateScreen
                    }
                    sideEffectsChannel.send(effect)
                }
            }

            Action.OnCreateProductClick -> {
                viewModelScope.launch {
                    val effect = if (isUserAuthenticated()) {
                        SideEffect.NavigateToCreateProduct
                    } else {
                        SideEffect.NavigateToAuthenticateScreen
                    }
                    sideEffectsChannel.send(effect)
                }
            }

            Action.OnCreateRecipeClick -> {
                viewModelScope.launch {
                    val effect = if (isUserAuthenticated()) {
                        SideEffect.NavigateToCreateRecipe
                    } else {
                        SideEffect.NavigateToAuthenticateScreen
                    }
                    sideEffectsChannel.send(effect)
                }
            }
        }
    }

    private suspend fun isUserAuthenticated(): Boolean {
        return getUserStatus().firstOrNull() != null
    }

    sealed class Action {
        data object OnCreatePlaceClick : Action()
        data object OnCreateRecipeClick : Action()
        data object OnCreateProductClick : Action()
    }

    sealed class SideEffect {
        data object NavigateToAuthenticateScreen : SideEffect()
        data object NavigateToCreatePlace : SideEffect()
        data object NavigateToCreateRecipe : SideEffect()
        data object NavigateToCreateProduct : SideEffect()
    }
}