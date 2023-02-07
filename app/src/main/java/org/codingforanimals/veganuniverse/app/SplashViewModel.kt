package org.codingforanimals.veganuniverse.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.codingforanimals.registration.presentation.RegistrationPresenter
import org.codingforanimals.veganuniverse.community.presentation.navigation.CommunityDestination
import org.codingforanimals.veganuniverse.core.ui.navigation.Destination
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingPresenter
import org.codingforanimals.veganuniverse.registration.presentation.navigation.WelcomeDestination

class SplashViewModel(
    private val onboardingPresenter: OnboardingPresenter,
    private val registrationPresenter: RegistrationPresenter,
) : ViewModel() {

    private val _uiState: MutableStateFlow<LaunchState> = MutableStateFlow(LaunchState.Loading)
    val uiState: StateFlow<LaunchState> = _uiState

    init {
        viewModelScope.launch {
            val showOnboarding = async { onboardingPresenter.showOnboarding() }
            val isUserLoggedIn = async { registrationPresenter.isUserLoggedIn() }
            _uiState.value = LaunchState.Completed(
                showOnboarding = showOnboarding.await(),
                isUserLoggedIn = isUserLoggedIn.await(),
                startDestination = if (isUserLoggedIn.await()) CommunityDestination else WelcomeDestination
            )
        }
    }

    sealed class LaunchState {
        object Loading : LaunchState()
        data class Completed(
            val showOnboarding: Boolean,
            val isUserLoggedIn: Boolean,
            val startDestination: Destination,
        ) : LaunchState()
    }
}