package org.codingforanimals.veganuniverse.registration.presentation.prompt

import android.app.Activity
import android.content.Intent
import android.util.Log
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
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.EmailRegistrationViewModel
import org.codingforanimals.veganuniverse.registration.presentation.model.RegistrationStatus
import org.codingforanimals.veganuniverse.registration.presentation.prompt.usecase.GetPromptScreenContentUseCase
import org.codingforanimals.veganuniverse.registration.presentation.prompt.usecase.GmailAuthenticationUseCase
import org.codingforanimals.veganuniverse.registration.presentation.prompt.viewmodel.AuthProvider

class PromptScreenViewModel(
    getPromptScreenContent: GetPromptScreenContentUseCase,
    private val gmailAuthenticationUseCase: GmailAuthenticationUseCase,
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
                    SideEffect.LaunchProviderActivity(gmailAuthenticationUseCase.intent)
                viewModelScope.launch { _sideEffects.send(effect) }
            }
        }
    }

    private fun attemptProviderAuthentication(result: ActivityResult) {
        val intent = result.data
        val a = intent?.extras?.keySet()?.map { it to (intent.extras?.get(it)).toString() }
        Log.e("pepe", "error intent $a")
        if (result.resultCode == Activity.RESULT_OK && intent != null) {
            authenticateWithGmail(intent)
        }
    }

    private fun authenticateWithGmail(intent: Intent) {
        viewModelScope.launch {
            gmailAuthenticationUseCase(intent).collect { status ->
                when (status) {
                    RegistrationStatus.Loading -> {
                        uiState = uiState.copy(loading = true)
                    }
                    RegistrationStatus.Success -> {
                        uiState = uiState.copy(loading = false)
                        viewModelScope.launch {
                            _sideEffects.send(SideEffect.NavigateToOriginDestination)
                        }
                    }
                    is RegistrationStatus.Exception -> {
                        uiState = uiState.copy(
                            loading = false,
                            errorDialog = EmailRegistrationViewModel.ErrorDialog(
                                title = status.title,
                                message = status.message,
                            )
                        )
                    }
                }
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val errorDialog: EmailRegistrationViewModel.ErrorDialog? = null,
        val text: String = "null",
    )

    sealed class Action {
        data object OnRegisterButtonClick : Action()
        data object OnSignInButtonClick : Action()
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