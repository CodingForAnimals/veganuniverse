package org.codingforanimals.veganuniverse.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.community.presentation.navigation.CommunityDestination
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingPresenter
import org.codingforanimals.veganuniverse.registration.presentation.navigation.WelcomeDestination
import org.codingforanimals.veganuniverse.user.GuestUser
import org.codingforanimals.veganuniverse.user.IllegalUserState
import org.codingforanimals.veganuniverse.user.LoggedUser
import org.codingforanimals.veganuniverse.user.UserRepository

class MainViewModel(
    private val onboardingPresenter: OnboardingPresenter,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<LaunchState> = MutableStateFlow(LaunchState.Loading)
    val uiState: StateFlow<LaunchState> = _uiState

    init {
        checkUserState()
    }

    private fun checkUserState() {
        viewModelScope.launch {
            val showOnboarding = async { onboardingPresenter.showOnboarding() }
            userRepository.user.collect { user ->
                val isUserLoggedIn = when (user) {
                    is LoggedUser -> true
                    GuestUser -> false
                    else -> throw IllegalUserState(user)
                }
                val startDestination =
                    if (isUserLoggedIn) CommunityDestination else WelcomeDestination
                _uiState.value = LaunchState.Completed(
                    showOnboarding = showOnboarding.await(),
                    startDestination = startDestination
                )
                cancel()
            }
        }
    }

    sealed class LaunchState {
        object Loading : LaunchState()
        data class Completed(
            val showOnboarding: Boolean,
            val startDestination: Destination,
        ) : LaunchState()
    }
}