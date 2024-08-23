package org.codingforanimals.veganuniverse.registration.presentation.prompt

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.ui.R.string.unexpected_error
import org.codingforanimals.veganuniverse.registration.presentation.R
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.EmailRegistrationViewModel
import org.codingforanimals.veganuniverse.registration.presentation.prompt.usecase.GetPromptScreenContentUseCase
import org.codingforanimals.veganuniverse.registration.presentation.prompt.viewmodel.AuthProvider
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.AuthenticationUseCases

class PromptScreenViewModel(
    getPromptScreenContent: GetPromptScreenContentUseCase,
    private val authenticationUseCases: AuthenticationUseCases,
) : ViewModel() {

    private val _sideEffects: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    val content = getPromptScreenContent()

    var uiState by mutableStateOf(UiState())
        private set

    fun onAction(action: Action) {
        when (action) {
            Action.OnRegisterButtonClick -> navigateToEmailRegistration()
            is Action.OnProviderAuthButtonClick -> launchProviderAuthActivity(action.provider)
            is Action.OnProviderAuthActivityFinished -> attemptProviderAuthentication(action.result)
            Action.OnSignInButtonClick -> navigateToEmailSignIn()
            Action.OnDismissErrorDialogRequest -> dismissErrorDialog()
        }
    }

    private fun navigateToEmailRegistration() {
        viewModelScope.launch {
            _sideEffects.send(SideEffect.NavigateToEmailRegistration)
        }
    }

    private fun navigateToEmailSignIn() {
        viewModelScope.launch {
            _sideEffects.send(SideEffect.NavigateToEmailSignIn)
        }
    }

    private fun launchProviderAuthActivity(provider: AuthProvider) {
        when (provider) {
            AuthProvider.Gmail -> {
                val effect =
                    SideEffect.LaunchProviderActivity(authenticationUseCases.googleSignInIntent)
                viewModelScope.launch { _sideEffects.send(effect) }
            }
        }
    }

    private fun attemptProviderAuthentication(result: ActivityResult) {
        val intent = result.data
        if (result.resultCode == Activity.RESULT_OK && intent != null) {
            authenticateWithGmail(intent)
        }
    }

    private fun authenticateWithGmail(intent: Intent) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)
            val result = authenticationUseCases.authenticateWithGmail(intent)
            uiState = uiState.copy(loading = false)
            if (result.isSuccess) {
                viewModelScope.launch {
                    _sideEffects.send(SideEffect.NavigateToOriginDestination)
                }
            } else {
                uiState = uiState.copy(
                    errorDialog = EmailRegistrationViewModel.ErrorDialog(
                        title = unexpected_error,
                        message = R.string.gmail_auth_error,
                    )
                )
            }
        }
    }

    private fun dismissErrorDialog() {
        uiState = uiState.copy(errorDialog = null)
    }

    data class UiState(
        val loading: Boolean = false,
        val errorDialog: EmailRegistrationViewModel.ErrorDialog? = null,
    )

    sealed class Action {
        data object OnRegisterButtonClick : Action()
        data object OnSignInButtonClick : Action()
        data object OnDismissErrorDialogRequest : Action()

        data class OnProviderAuthButtonClick(val provider: AuthProvider) : Action()

        data class OnProviderAuthActivityFinished(val result: ActivityResult) : Action()
    }

    sealed class SideEffect {
        data object NavigateToEmailRegistration : SideEffect()
        data object NavigateToEmailSignIn : SideEffect()
        data object NavigateToOriginDestination : SideEffect()
        data class LaunchProviderActivity(val intent: Intent) : SideEffect()
    }
}