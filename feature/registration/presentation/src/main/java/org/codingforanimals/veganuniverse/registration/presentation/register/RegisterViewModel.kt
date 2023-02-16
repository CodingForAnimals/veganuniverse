package org.codingforanimals.veganuniverse.registration.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.user.UserRepository

class RegisterViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _sideEffects = Channel<SideEffect>()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    fun onAction(action: Action) {
        when (action) {
            Action.RegistrationAttempt -> attemptRegistration()
            Action.AlmostThereDismissed -> viewModelScope.launch { _sideEffects.send(SideEffect.NavigateToCommunity) }
        }
    }

    private fun attemptRegistration() {
        viewModelScope.launch {
            userRepository.login()
                .catch {
                    _sideEffects.send(SideEffect.ShowRegisterError)
                }
                .onStart {}
                .collectLatest {
                    _sideEffects.send(SideEffect.ShowAlmostThereContent)
                }
        }
    }

    sealed class Action {
        object RegistrationAttempt : Action()
        object AlmostThereDismissed : Action()
    }

    sealed class SideEffect {
        object ShowAlmostThereContent : SideEffect()
        object ShowRegisterError : SideEffect()
        object NavigateToCommunity : SideEffect()
    }
}