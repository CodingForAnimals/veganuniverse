package org.codingforanimals.veganuniverse.registration.presentation.emailregistration

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
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.usecase.GetEmailRegistrationScreenContent
import org.codingforanimals.veganuniverse.registration.presentation.viewmodel.EmailField
import org.codingforanimals.veganuniverse.registration.presentation.viewmodel.PasswordField
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.StringField
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.areFieldsValid
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.AuthenticationUseCases

class EmailRegistrationViewModel(
    getRegisterScreenContent: GetEmailRegistrationScreenContent,
    private val authenticationUseCases: AuthenticationUseCases,
) : ViewModel() {

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
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val result = authenticationUseCases.createUserWithEmailAndPassword(
                email = uiState.emailField.value,
                password = uiState.passwordField.value,
                name = uiState.usernameField.value,
            )
            uiState = uiState.copy(isLoading = false)
            if (result.isSuccess) {
                _sideEffects.send(SideEffect.NavigateToValidateEmailScreen)
            } else {
                uiState = uiState.copy(
                    errorDialog = ErrorDialog(
                        title = unexpected_error,
                        message = R.string.email_registration_error,
                    )
                )
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