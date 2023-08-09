package org.codingforanimals.veganuniverse.registration.presentation.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import org.codingforanimals.veganuniverse.core.common.R.string.back
import org.codingforanimals.veganuniverse.core.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.core.ui.components.VUTextField
import org.codingforanimals.veganuniverse.core.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_07
import org.codingforanimals.veganuniverse.registration.presentation.R
import org.codingforanimals.veganuniverse.registration.presentation.signin.EmailSignInViewModel.Action
import org.codingforanimals.veganuniverse.registration.presentation.signin.EmailSignInViewModel.UiState
import org.codingforanimals.veganuniverse.registration.presentation.signin.viewmodel.EmailSignInScreenItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmailSignInScreen(
    navigateToOriginDestination: () -> Unit,
    viewModel: EmailSignInViewModel = koinViewModel(),
) {

    Column {
        VUTopAppBar(
            title = stringResource(R.string.email_sign_in_topbar_text),
            onBackClick = navigateToOriginDestination,
        )

        EmailSignInScreen(
            content = viewModel.content,
            uiState = viewModel.uiState,
            onAction = viewModel::onAction,
        )
    }

    VUCircularProgressIndicator(visible = viewModel.uiState.loading)

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
private fun EmailSignInScreen(
    content: List<EmailSignInScreenItem>,
    uiState: UiState,
    onAction: (Action) -> Unit,
) = with(uiState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing_06),
        verticalArrangement = Arrangement.spacedBy(Spacing_07),
        content = {
            items(
                items = content,
                itemContent = { item ->
                    when (item) {
                        EmailSignInScreenItem.Title -> Text(
                            text = stringResource(R.string.email_sign_in_title),
                            style = MaterialTheme.typography.titleLarge,
                        )
                        EmailSignInScreenItem.Email -> VUTextField(
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
                        EmailSignInScreenItem.Password -> VUTextField(
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
                        EmailSignInScreenItem.SignInButton -> Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                            onClick = { onAction(Action.OnSignInButtonClick) },
                            content = { Text(text = stringResource(R.string.sign_in_button_label)) },
                        )
                    }
                },
            )
        },
    )
}