package org.codingforanimals.veganuniverse.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileScreenViewModel: ViewModel() {

    private val _sideEffect = Channel<SideEffect>()
    val sideEffect: Flow<SideEffect> = _sideEffect.receiveAsFlow()

    fun onRegisterClick() {
        viewModelScope.launch {
            _sideEffect.send(SideEffect.NavigateToRegister)
        }
    }

    sealed class SideEffect {
        object NavigateToRegister: SideEffect()
    }
}