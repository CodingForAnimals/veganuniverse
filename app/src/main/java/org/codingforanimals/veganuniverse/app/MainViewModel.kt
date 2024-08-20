package org.codingforanimals.veganuniverse.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.navigation.DeeplinkNavigator
import org.codingforanimals.veganuniverse.commons.profile.domain.repository.ProfileRepository
import org.codingforanimals.veganuniverse.commons.user.domain.repository.CurrentUserRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.AuthenticationUseCases
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.onboarding.domain.OnboardingRepository
import org.codingforanimals.veganuniverse.services.location.UserLocationManager

class UserVerificationUseCases(
    private val currentUserRepository: CurrentUserRepository,
    private val authenticationUseCases: AuthenticationUseCases,
) {
    suspend fun evaluateUserVerification(): Result {
        return runCatching {
            val isUserVerifiedLocal = currentUserRepository.flowOnIsUserVerified().firstOrNull()
                ?: return Result.UnauthenticatedUser
            if (isUserVerifiedLocal) {
                return Result.UserIsAlreadyVerified
            }
            return if (currentUserRepository.refreshIsUserVerified()) {
                authenticationUseCases.logout()
                Result.UserHasJustVerifiedEmail
            } else {
                Result.UserHasNotYetVerifiedEmail
            }
        }.getOrElse {
            Log.e("UserVerification", it.stackTraceToString())
            authenticationUseCases.logout()
            Result.UnexpectedError
        }
    }

    sealed class Result {
        data object UnexpectedError : Result()
        data object UnauthenticatedUser : Result()
        data object UserIsAlreadyVerified : Result()
        data object UserHasJustVerifiedEmail : Result()
        data object UserHasNotYetVerifiedEmail : Result()
    }
}

class MainViewModel(
    private val userLocationManager: UserLocationManager,
    private val profileRepository: ProfileRepository,
    private val deeplinkNavigator: DeeplinkNavigator,
    flowOnCurrentUser: FlowOnCurrentUser,
    private val onboardingRepository: OnboardingRepository,
    private val userVerificationUseCases: UserVerificationUseCases,
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
            when (userVerificationUseCases.evaluateUserVerification()) {
                UserVerificationUseCases.Result.UnexpectedError,
                UserVerificationUseCases.Result.UnauthenticatedUser,
                UserVerificationUseCases.Result.UserHasNotYetVerifiedEmail,
                UserVerificationUseCases.Result.UserIsAlreadyVerified -> Unit

                UserVerificationUseCases.Result.UserHasJustVerifiedEmail -> {
                    deeplinkNavigator.navigate(DeepLink.Reauthentication)
                }
            }
        }
    }
}
