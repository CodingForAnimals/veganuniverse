package org.codingforanimals.veganuniverse.registration.presentation.emailvalidation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.AuthenticationUseCases
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.SendVerificationEmail
import org.codingforanimals.veganuniverse.commons.user.presentation.R

internal class EmailValidationViewModel(
    private val sendVerificationEmail: SendVerificationEmail,
    private val authenticationUseCases: AuthenticationUseCases,
) : ViewModel() {

    private val navigationEffectsChannel = Channel<NavigationEffect>()
    val navigationEffects = navigationEffectsChannel.receiveAsFlow()

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    var loading by mutableStateOf(false)
        private set

    fun onAction(action: Action) {
        when (action) {
            Action.OnNavigateUpClick,
            Action.OnContinueAsGuestClick -> {
                viewModelScope.launch {
                    navigationEffectsChannel.send(NavigationEffect.NavigateUp)
                }
            }

            Action.OnResendEmailClick -> {
                loading = true
                viewModelScope.launch {
                    if (sendVerificationEmail().isSuccess) {
                        snackbarEffectsChannel.send(Snackbar(R.string.verification_email_sent))
                    } else {
                        snackbarEffectsChannel.send(Snackbar(R.string.verification_email_not_sent))
                    }
                    loading = false
                }
            }

            Action.OnEmailValidatedClick -> {
                viewModelScope.launch {
                    authenticationUseCases.logout()
                    navigationEffectsChannel.send(NavigationEffect.NavigateToEmailSignIn)
                }
            }
        }
    }

    sealed class Action {
        data object OnNavigateUpClick : Action()
        data object OnEmailValidatedClick : Action()
        data object OnResendEmailClick : Action()
        data object OnContinueAsGuestClick : Action()
    }

    sealed class NavigationEffect {
        data object NavigateToEmailSignIn : NavigationEffect()
        data object NavigateUp : NavigationEffect()
    }
}