package org.codingforanimals.veganuniverse.registration.presentation.emailsignin

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.registration.presentation.emailsignin.usecase.EmailSignInUseCase
import org.codingforanimals.veganuniverse.registration.presentation.emailsignin.usecase.GetEmailSignInScreenContent
import org.codingforanimals.veganuniverse.registration.presentation.model.EmailSignInStatus
import org.codingforanimals.veganuniverse.registration.presentation.viewmodel.EmailField
import org.codingforanimals.veganuniverse.registration.presentation.viewmodel.PasswordField
import org.codingforanimals.veganuniverse.ui.viewmodel.areFieldsValid

class EmailSignInViewModel(
    getEmailSignInScreenContent: GetEmailSignInScreenContent,
    private val emailSignInUseCase: EmailSignInUseCase,
) : ViewModel() {

    val content = getEmailSignInScreenContent()

    private val _sideEffects: Channel<SideEffect> = Channel()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    var uiState by mutableStateOf(UiState())
        private set

    fun onAction(action: Action) {
        when (action) {
            is Action.OnFormChange -> updateForm(action)
            Action.OnSignInButtonClick -> attemptSignIn()
            Action.OnErrorDialogDismissRequest -> dismissErrorDialog()
        }
    }

    private fun updateForm(action: Action.OnFormChange) {
        action.email?.let { uiState = uiState.copy(emailField = EmailField(it)) }
        action.password?.let { uiState = uiState.copy(passwordField = PasswordField(it)) }
    }

    private fun attemptSignIn() {
        if (uiState.areFieldsValid) {
            signIn()
        } else {
            uiState = uiState.copy(isValidating = true)
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            emailSignInUseCase(
                email = uiState.emailField.value,
                password = uiState.passwordField.value,
            ).collect { status ->
                when (status) {
                    EmailSignInStatus.Loading -> {
                        uiState = uiState.copy(loading = true)
                    }

                    EmailSignInStatus.Success -> {
                        uiState = uiState.copy(loading = false)
                        _sideEffects.send(SideEffect.NavigateToOriginDestination)
                    }

                    is EmailSignInStatus.Exception -> {
                        uiState = uiState.copy(
                            loading = false,
                            errorDialog = ErrorDialog(
                                title = status.title,
                                message = status.message,
                            )
                        )
                    }
                }
            }
        }
    }

    private fun dismissErrorDialog() {
        uiState = uiState.copy(errorDialog = null)
    }

    data class UiState(
        val loading: Boolean = false,
        val isValidating: Boolean = false,
        val errorDialog: ErrorDialog? = null,
        val emailField: EmailField = EmailField(),
        val passwordField: PasswordField = PasswordField(),
    ) {
        val areFieldsValid = areFieldsValid(emailField, passwordField)
    }

    data class ErrorDialog(
        @StringRes val title: Int,
        @StringRes val message: Int,
    )

    sealed class Action {
        data class OnFormChange(
            val email: String? = null,
            val password: String? = null,
        ) : Action()

        data object OnSignInButtonClick : Action()
        data object OnErrorDialogDismissRequest : Action()
    }

    sealed class SideEffect {
        data object NavigateToOriginDestination : SideEffect()
    }
}