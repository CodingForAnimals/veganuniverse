package org.codingforanimals.veganuniverse.registration.presentation.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class RegisterViewModel: ViewModel() {

    private val _sideEffects = Channel<SideEffect>()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    sealed class SideEffect {
        object ShowAlmostThereContent: SideEffect()
        object NavigateToCommunity: SideEffect()
    }
}