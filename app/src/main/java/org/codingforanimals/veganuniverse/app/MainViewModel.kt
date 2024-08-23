package org.codingforanimals.veganuniverse.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.navigation.DeeplinkNavigator
import org.codingforanimals.veganuniverse.commons.profile.domain.repository.ProfileRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingPresenter
import org.codingforanimals.veganuniverse.services.location.UserLocationManager

class MainViewModel(
    private val onboardingPresenter: OnboardingPresenter,
    private val userLocationManager: UserLocationManager,
    private val profileRepository: ProfileRepository,
    deeplinkNavigator: DeeplinkNavigator,
    flowOnCurrentUser: FlowOnCurrentUser,
) : ViewModel() {

    val deeplinkFlow = deeplinkNavigator.deeplinkFlow

    private val _uiState: MutableStateFlow<LaunchState> = MutableStateFlow(LaunchState.Loading)
    val uiState: StateFlow<LaunchState> = _uiState

    init {
        viewModelScope.launch {
            flowOnCurrentUser().collectLatest { user ->
                if (user == null) {
                    Log.e("pepe", "clearing")
                    profileRepository.clearProfile()
                } else {
                    Log.e("pepe", "downloading")
                    profileRepository.downloadAndStoreProfile()
                }
            }
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