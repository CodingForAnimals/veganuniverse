package org.codingforanimals.veganuniverse.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.navigation.DeeplinkNavigator
import org.codingforanimals.veganuniverse.commons.navigation.model.DeepLinkNavigationOptions
import org.codingforanimals.veganuniverse.commons.profile.domain.repository.ProfileRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.EvaluateUserEmailVerificationState
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.onboarding.domain.OnboardingRepository
import org.codingforanimals.veganuniverse.registration.presentation.navigation.RegistrationDestination
import org.codingforanimals.veganuniverse.services.location.UserLocationManager

class MainViewModel(
    private val userLocationManager: UserLocationManager,
    private val profileRepository: ProfileRepository,
    private val deeplinkNavigator: DeeplinkNavigator,
    flowOnCurrentUser: FlowOnCurrentUser,
    private val onboardingRepository: OnboardingRepository,
    private val evaluateUserEmailVerificationState: EvaluateUserEmailVerificationState,
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

    fun evaluateUserVerification() {
        viewModelScope.launch {
            when (evaluateUserEmailVerificationState()) {
                EvaluateUserEmailVerificationState.Result.UnexpectedError,
                EvaluateUserEmailVerificationState.Result.UnauthenticatedUser,
                EvaluateUserEmailVerificationState.Result.UserHasNotYetVerifiedEmail,
                EvaluateUserEmailVerificationState.Result.UserIsAlreadyVerified -> Unit

                EvaluateUserEmailVerificationState.Result.UserHasJustVerifiedEmail -> {
                    val options = DeepLinkNavigationOptions.Builder()
                        .popUpTo(
                            route = RegistrationDestination.ValidateEmailPrompt.route,
                            inclusive = true,
                        ).build()
                    deeplinkNavigator.navigate(DeepLink.EmailValidated, options)
                }
            }
        }
    }
}
