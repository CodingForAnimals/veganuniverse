package org.codingforanimals.veganuniverse.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class OnboardingViewModel : ViewModel() {

    private val _sideEffect: Channel<SideEffect> = Channel()
    val sideEffect: Flow<SideEffect> = _sideEffect.receiveAsFlow()

    fun onAction(action: Action) {
        when (action) {
            Action.OnUserDismissOnboardingScreen -> {
                viewModelScope.launch {
                    _sideEffect.send(SideEffect.DismissOnboardingScreen)
                }
            }
        }
    }

    sealed class Action {
        object OnUserDismissOnboardingScreen : Action()
    }

    sealed class SideEffect {
        object DismissOnboardingScreen : SideEffect()
    }
}