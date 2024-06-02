package org.codingforanimals.veganuniverse.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.core.location.UserLocationManager
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingPresenter
import org.codingforanimals.veganuniverse.profile.domain.repository.ProfileRepository

class MainViewModel(
    private val onboardingPresenter: OnboardingPresenter,
    private val userLocationManager: UserLocationManager,
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<LaunchState> = MutableStateFlow(LaunchState.Loading)
    val uiState: StateFlow<LaunchState> = _uiState

    init {
        viewModelScope.launch {
            profileRepository.downloadAndStoreProfile()
        }
    }

    fun onPermissionsChange(fineLocation: Boolean?) {
        checkUserState()
        if (fineLocation == true) {
            viewModelScope.launch {
                userLocationManager.requestUserLocation()
            }
        }
    }

    private fun checkUserState() {
        viewModelScope.launch {
            val showOnboarding = async { onboardingPresenter.showOnboarding() }
            _uiState.value = LaunchState.Completed(
                showOnboarding = showOnboarding.await(),
            )
        }
    }

    sealed class LaunchState {
        data object Loading : LaunchState()
        data class Completed(
            val showOnboarding: Boolean,
        ) : LaunchState()
    }
}