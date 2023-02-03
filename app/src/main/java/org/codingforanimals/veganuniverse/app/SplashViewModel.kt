package org.codingforanimals.veganuniverse.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingPresenter

class SplashViewModel(
    private val onboardingPresenter: OnboardingPresenter,
) : ViewModel() {

    private val _uiState: MutableStateFlow<SplashUiState> = MutableStateFlow(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = SplashUiState.Completed(onboardingPresenter.showOnboarding())
        }
    }

    fun onboardingScreenDismissed() {
        viewModelScope.launch {
            onboardingPresenter.setOnboardingAsDismissed()
        }
    }

    sealed class SplashUiState {
        object Loading : SplashUiState()
        data class Completed(val showOnboarding: Boolean) : SplashUiState()
    }
}