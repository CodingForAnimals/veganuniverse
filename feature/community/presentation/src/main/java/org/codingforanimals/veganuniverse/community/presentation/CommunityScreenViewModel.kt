package org.codingforanimals.veganuniverse.community.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.user.User
import org.codingforanimals.veganuniverse.user.UserRepository

class CommunityScreenViewModel(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        _uiState.value = UiState.Success(userRepository.user)
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(
            val user: User
        ) : UiState()
    }
}