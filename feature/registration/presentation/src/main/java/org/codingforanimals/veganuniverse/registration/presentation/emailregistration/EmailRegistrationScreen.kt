@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.registration.presentation.emailregistration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_07
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.registration.presentation.R
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.EmailRegistrationViewModel.Action
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.EmailRegistrationViewModel.UiState
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.usecase.GetEmailRegistrationScreenContent
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.viewmodel.EmailRegistrationScreenItem
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.components.VUTextField
import org.codingforanimals.veganuniverse.commons.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.commons.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun EmailRegistrationScreen(
    navigateUp: () -> Unit,
    navigateToEmailValidationScreen: () -> Unit,
    viewModel: EmailRegistrationViewModel = koinViewModel(),
) {
    HandleSideEffects(
        effectsFlow = viewModel.sideEffects,
        navigateToEmailValidationScreen = navigateToEmailValidationScreen,
    )

    Column {
        VUTopAppBar(
            title = "Crea tu usuario",
            onBackClick = navigateUp,
        )

        EmailRegistrationScreen(
            content = viewModel.content,
            uiState = viewModel.uiState,
            onAction = viewModel::onAction,
        )
    }

    VUCircularProgressIndicator(visible = viewModel.uiState.isLoading)

    viewModel.uiState.errorDialog?.let { errorDialog ->
        val onDismissRequest = { viewModel.onAction(Action.OnErrorDialogDismissRequest) }
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(stringResource(errorDialog.title)) },
            text = { Text(stringResource(errorDialog.message)) },
            confirmButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(stringResource(back))
                }
            },
        )
    }
}

@Composable
private fun EmailRegistrationScreen(
    content: List<EmailRegistrationScreenItem>,
    uiState: UiState,
    onAction: (Action) -> Unit,
) = with(uiState) {
    val focusManager = LocalFocusManager.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing_06, vertical = Spacing_06),
        verticalArrangement = Arrangement.spacedBy(Spacing_07)
    ) {
        items(items = content) { item ->
            when (item) {
                EmailRegistrationScreenItem.ScreenTitle -> Text(
                    text = stringResource(R.string.register_screen_title),
                    style = MaterialTheme.typography.titleLarge
                )

                EmailRegistrationScreenItem.EmailInputField -> VUTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = emailField.value,
                    onValueChange = { onAction(Action.OnFormChange(email = it)) },
                    placeholder = stringResource(R.string.email_field_placeholder),
                    leadingIcon = VUIcons.Email,
                    isError = isValidating && !emailField.isValid,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    maxLines = 1,
                )

                EmailRegistrationScreenItem.UsernameInputField -> VUTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = usernameField.value,
                    onValueChange = { onAction(Action.OnFormChange(username = it)) },
                    placeholder = stringResource(R.string.register_screen_username_placeholder),
                    leadingIcon = VUIcons.Profile,
                    isError = isValidating && !usernameField.isValid,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                    maxLines = 1,
                )

                EmailRegistrationScreenItem.PasswordInputField -> VUTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordField.value,
                    onValueChange = { onAction(Action.OnFormChange(password = it)) },
                    placeholder = stringResource(R.string.password_field_placeholder),
                    leadingIcon = VUIcons.Lock,
                    isError = isValidating && !passwordField.isValid,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        autoCorrect = false,
                        imeAction = ImeAction.Next,
                    ),
                    maxLines = 1,
                )

                EmailRegistrationScreenItem.ConfirmPasswordInputField -> VUTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmPasswordField.value,
                    onValueChange = { onAction(Action.OnFormChange(confirmPassword = it)) },
                    placeholder = stringResource(R.string.password_field_placeholder),
                    leadingIcon = VUIcons.Lock,
                    isError = isValidating && !confirmPasswordField.isValid,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        autoCorrect = false,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    maxLines = 1,
                )

                EmailRegistrationScreenItem.EmailRegistrationButton -> Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    onClick = { onAction(Action.OnCreateAccountButtonClick) },
                    content = { Text(text = "Registrarte") },
                )
            }
        }
    }
}

@Composable
private fun HandleSideEffects(
    effectsFlow: Flow<EmailRegistrationViewModel.SideEffect>,
    navigateToEmailValidationScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        effectsFlow.onEach { effect ->
            when (effect) {
                EmailRegistrationViewModel.SideEffect.NavigateToValidateEmailScreen -> {
                    navigateToEmailValidationScreen()
                }
            }
        }.collect()
    }
}

@Preview
@Composable
private fun PreviewRegisterScreen() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            EmailRegistrationScreen(
                content = GetEmailRegistrationScreenContent().invoke(),
                uiState = UiState(),
                onAction = {},
            )
        }
    }
}