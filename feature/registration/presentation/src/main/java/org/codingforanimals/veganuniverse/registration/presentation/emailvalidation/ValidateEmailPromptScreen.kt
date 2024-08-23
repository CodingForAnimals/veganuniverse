@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.registration.presentation.emailvalidation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_10
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.registration.presentation.R
import org.codingforanimals.veganuniverse.registration.presentation.components.SecondaryAuthOptionDivider
import org.codingforanimals.veganuniverse.registration.presentation.emailvalidation.EmailValidationViewModel.Action
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ValidateEmailPromptScreen(
    navigateToEmailSignIn: () -> Unit,
    navigateUp: () -> Unit,
) {
    val viewModel: EmailValidationViewModel = koinViewModel()

    val snackbarHostState = remember { SnackbarHostState() }

    ValidateEmailPromptScreen(
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState,
        isLoading = viewModel.loading,
    )

    HandleSnackbarEffects(
        snackbarEffects = viewModel.snackbarEffects, snackbarHostState = snackbarHostState
    )

    LaunchedEffect(Unit) {
        viewModel.navigationEffects.onEach { navigationEffect ->
            when (navigationEffect) {
                EmailValidationViewModel.NavigationEffect.NavigateUp -> navigateUp()
                EmailValidationViewModel.NavigationEffect.NavigateToEmailSignIn -> navigateToEmailSignIn()
            }
        }.collect()
    }
}

@Composable
private fun ValidateEmailPromptScreen(
    onAction: (Action) -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    isLoading: Boolean = false,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { onAction(Action.OnNavigateUpClick) }) {
                        Icon(
                            imageVector = VUIcons.ArrowBack.imageVector,
                            contentDescription = stringResource(id = R.string.back),
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(Spacing_06)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.validate_email_screen_title),
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                modifier = Modifier.padding(top = Spacing_06),
                text = stringResource(R.string.validate_email_screen_message),
                style = MaterialTheme.typography.bodyMedium,
            )
            Icon(
                modifier = Modifier
                    .padding(vertical = Spacing_10)
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = VUIcons.Email.id),
                contentDescription = null,
            )
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                enabled = !isLoading,
                onClick = { onAction(Action.OnEmailValidatedClick) },
                content = {
                    Text(text = stringResource(id = R.string.validate_email_screen_action))
                }
            )
            TextButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = Spacing_04),
                enabled = !isLoading,
                onClick = { onAction(Action.OnResendEmailClick) },
                content = {
                    Text(text = stringResource(R.string.reenviar_correo))
                },
            )
            SecondaryAuthOptionDivider()
            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                enabled = !isLoading,
                onClick = { onAction(Action.OnContinueAsGuestClick) },
                content = {
                    Text(text = stringResource(R.string.continue_as_guest))
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewEmailValidationScreen() {
    VeganUniverseTheme {
        ValidateEmailPromptScreen()
    }
}
