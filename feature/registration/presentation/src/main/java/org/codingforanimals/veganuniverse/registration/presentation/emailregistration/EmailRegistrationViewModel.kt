package org.codingforanimals.veganuniverse.registration.presentation.emailregistration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.usecase.EmailAndPasswordRegistrationUseCase
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.usecase.GetEmailRegistrationScreenContent
import org.codingforanimals.veganuniverse.registration.presentation.model.RegistrationStatus
import org.codingforanimals.veganuniverse.registration.presentation.viewmodel.EmailField
import org.codingforanimals.veganuniverse.registration.presentation.viewmodel.PasswordField
import org.codingforanimals.veganuniverse.ui.viewmodel.StringField
import org.codingforanimals.veganuniverse.ui.viewmodel.areFieldsValid

class EmailRegistrationViewModel(
    getRegisterScreenContent: GetEmailRegistrationScreenContent,
    private val emailAndPasswordRegistration: EmailAndPasswordRegistrationUseCase,
) : ViewModel() {

    private var createAccountJob: Job? = null

    private val _sideEffects = Channel<SideEffect>()
    val sideEffects: Flow<SideEffect> = _sideEffects.receiveAsFlow()

    val content = getRegisterScreenContent()

    var uiState by mutableStateOf(UiState())

    fun onAction(action: Action) {
        when (action) {
            is Action.OnFormChange -> updateForm(action)
            Action.OnCreateAccountButtonClick -> registrationAttempt()
            Action.OnErrorDialogDismissRequest -> dismissErrorDialog()
        }
    }

    private fun updateForm(action: Action.OnFormChange) {
        action.email?.let { uiState = uiState.copy(emailField = EmailField(it)) }
        action.username?.let { uiState = uiState.copy(usernameField = StringField(it)) }
        action.password?.let { uiState = uiState.copy(passwordField = PasswordField(it)) }
        action.confirmPassword?.let {
            uiState = uiState.copy(confirmPasswordField = PasswordField(it))
        }
    }

    private fun registrationAttempt() {
        if (uiState.areFieldsValid) {
            registerUser()
        } else {
            uiState = uiState.copy(isValidating = true)
        }
    }

    private fun registerUser() {
        createAccountJob?.cancel()
        createAccountJob = viewModelScope.launch {
            emailAndPasswordRegistration(
                email = uiState.emailField.value,
                password = uiState.passwordField.value,
                name = uiState.usernameField.value,
            ).collectLatest { status ->
                when (status) {
                    RegistrationStatus.Loading -> {
                        uiState = uiState.copy(isLoading = true)
                    }

                    RegistrationStatus.Success -> {
                        uiState = uiState.copy(isLoading = false)
                        _sideEffects.send(SideEffect.NavigateToValidateEmailScreen)
                    }

                    is RegistrationStatus.Exception -> {
                        uiState = uiState.copy(
                            isLoading = false,
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
        val isValidating: Boolean = false,
        val isLoading: Boolean = false,
        val errorDialog: ErrorDialog? = null,
        val emailField: EmailField = EmailField(),
        val usernameField: StringField = StringField(),
        val passwordField: PasswordField = PasswordField(),
        val confirmPasswordField: PasswordField = PasswordField(),
    ) {
        val areFieldsValid =
            areFieldsValid(
                emailField,
                usernameField,
                passwordField,
                confirmPasswordField
            ) && passwordField.value == confirmPasswordField.value
    }

    data class ErrorDialog(
        val title: Int,
        val message: Int,
    )

    sealed class Action {
        data class OnFormChange(
            val email: String? = null,
            val username: String? = null,
            val password: String? = null,
            val confirmPassword: String? = null,
        ) : Action()

        data object OnCreateAccountButtonClick : Action()
        data object OnErrorDialogDismissRequest : Action()
    }

    sealed class SideEffect {
        data object NavigateToValidateEmailScreen : SideEffect()
    }
}