package org.codingforanimals.veganuniverse.registration.presentation.prompt

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.EmailRegistrationViewModel
import org.codingforanimals.veganuniverse.registration.presentation.prompt.usecase.GetPromptScreenContent
import org.codingforanimals.veganuniverse.registration.presentation.prompt.usecase.GmailAuthenticationUseCase
import org.codingforanimals.veganuniverse.registration.presentation.prompt.viewmodel.AuthProvider
import org.codingforanimals.veganuniverse.registration.presentation.usecase.UserAuthStatus

class PromptScreenViewModel(
    getPromptScreenContent: GetPromptScreenContent,
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
            Action.Test -> {
                val u = Firebase.auth.currentUser
                Log.e("pepe", "current user $u")
            }
        }
    }

    private fun navigateToEmailRegistration() {
        viewModelScope.launch {
            _sideEffects.send(SideEffect.NavigateToEmailRegistration)
        }
    }

    private fun launchProviderAuthActivity(provider: AuthProvider) {
        when (provider) {
            AuthProvider.Gmail -> {
                val effect =
                    SideEffect.LaunchProviderActivity(gmailAuthenticationUseCase.intent)
                viewModelScope.launch { _sideEffects.send(effect) }
            }
            AuthProvider.Facebook -> {}
            AuthProvider.Twitter -> {}
        }
    }

    private fun attemptProviderAuthentication(result: ActivityResult) {
        viewModelScope.launch {
            gmailAuthenticationUseCase(result).collect { status ->
                when (status) {
                    UserAuthStatus.Loading -> {
                        uiState = uiState.copy(loading = true)
                    }
                    UserAuthStatus.Success -> {
                        uiState = uiState.copy(loading = false)
                    }
                    is UserAuthStatus.Exception -> {
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
    )

    sealed class Action {
        object OnRegisterButtonClick : Action()
        object Test : Action()
        data class OnProviderAuthButtonClick(val provider: AuthProvider) : Action()

        data class OnProviderAuthActivityFinished(val result: ActivityResult) : Action()
    }

    sealed class SideEffect {
        object NavigateToEmailRegistration : SideEffect()
        data class LaunchProviderActivity(val intent: Intent) : SideEffect()
    }
}