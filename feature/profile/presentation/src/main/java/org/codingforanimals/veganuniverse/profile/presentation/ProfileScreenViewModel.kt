package org.codingforanimals.veganuniverse.profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.user.UserRepository

class ProfileScreenViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _sideEffect = Channel<SideEffect>()
    val sideEffect: Flow<SideEffect> = _sideEffect.receiveAsFlow()

    var uiState by mutableStateOf(UiState())
        private set

    init {
        viewModelScope.launch {
            userRepository.user.collectLatest { user ->
                if (user != null) {
                    uiState = uiState.copy(
                        loggedIn = true,
                        uid = user.id,
                        name = user.name,
                        email = user.email,
                    )
                }
            }
        }
    }

    fun onAction(action: Action): Any {
        return when (action) {
            Action.OnCreateUserButtonClick -> navigateToRegisterScreen()
            Action.LogOut -> {
                viewModelScope.launch { userRepository.logout() }
            }
        }
    }

    private fun navigateToRegisterScreen() {
        viewModelScope.launch {
            _sideEffect.send(SideEffect.NavigateToRegister)
        }
    }

    data class UiState(
        val loggedIn: Boolean = false,
        val uid: String = "",
        val name: String = "",
        val email: String = "",
    )

    sealed class Action {
        object OnCreateUserButtonClick : Action()
        object LogOut : Action()
    }

    sealed class SideEffect {
        object NavigateToRegister : SideEffect()
    }
}