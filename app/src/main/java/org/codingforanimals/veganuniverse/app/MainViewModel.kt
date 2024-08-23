package org.codingforanimals.veganuniverse.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.navigation.DeeplinkNavigator
import org.codingforanimals.veganuniverse.commons.profile.domain.repository.ProfileRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.onboarding.domain.OnboardingRepository
import org.codingforanimals.veganuniverse.services.location.UserLocationManager

class MainViewModel(
    private val userLocationManager: UserLocationManager,
    private val profileRepository: ProfileRepository,
    deeplinkNavigator: DeeplinkNavigator,
    flowOnCurrentUser: FlowOnCurrentUser,
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {

    val wasOnboardingDismissed = onboardingRepository
        .wasOnboardingDismissed()
        .stateIn(
            scope = viewModelScope,
            initialValue = true,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    val deeplinkFlow = deeplinkNavigator.deeplinkFlow

    init {
        viewModelScope.launch {

            // pasar a un caso de uso.
            flowOnCurrentUser().collectLatest { user ->
                if (user == null) {
                    profileRepository.clearProfile()
                } else {
                    profileRepository.downloadAndStoreProfile()
                }
            }
        }
    }

    // pasar a place home screen
    fun onPermissionsChange(fineLocation: Boolean?) {
        if (fineLocation == true) {
            viewModelScope.launch {
                userLocationManager.requestUserLocation()
            }
        }
    }

    fun setOnboardingAsDismissed() {
        viewModelScope.launch {
            onboardingRepository.setWasOnboardingDismissed(true)
        }
    }
}
