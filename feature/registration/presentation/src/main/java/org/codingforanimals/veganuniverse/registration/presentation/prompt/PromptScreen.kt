package org.codingforanimals.veganuniverse.registration.presentation.prompt

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.core.common.R.string.back
import org.codingforanimals.veganuniverse.core.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.error.NoActionDialog
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_04
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_06
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_08
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_09
import org.codingforanimals.veganuniverse.registration.presentation.R
import org.codingforanimals.veganuniverse.registration.presentation.prompt.PromptScreenViewModel.Action
import org.codingforanimals.veganuniverse.registration.presentation.prompt.viewmodel.AuthProvider
import org.codingforanimals.veganuniverse.registration.presentation.prompt.viewmodel.RegistrationScreenItem
import org.koin.androidx.compose.koinViewModel

private const val TAG = "PromptScreen"

@Composable
internal fun PromptScreen(
    navigateUp: () -> Unit,
    navigateToEmailRegistration: () -> Unit,
    navigateToEmailSignIn: () -> Unit,
    navigateToOriginDestination: () -> Unit,
    viewModel: PromptScreenViewModel = koinViewModel(),
) {

    val providerSignInActivityLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            viewModel.onAction(Action.OnProviderAuthActivityFinished(result))
        }

    val uiState = viewModel.uiState

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateToEmailRegistration = navigateToEmailRegistration,
        navigateToEmailSignIn = navigateToEmailSignIn,
        navigateToOriginDestination = navigateToOriginDestination,
        providerSignInActivityLauncher = providerSignInActivityLauncher,
    )

    VUIcon(
        modifier = Modifier.padding(Spacing_06),
        icon = VUIcons.Close,
        contentDescription = "",
        onIconClick = navigateUp,
    )

    PromptScreen(
        content = viewModel.content,
        onAction = viewModel::onAction,
    )

    uiState.errorDialog?.let { errorDialog ->
        NoActionDialog(
            title = errorDialog.title,
            message = errorDialog.message,
            buttonText = back,
            onDismissRequest = { viewModel.onAction(Action.OnDismissErrorDialogRequest) }
        )
    }

    VUCircularProgressIndicator(visible = uiState.loading)
}

@Composable
private fun PromptScreen(
    content: List<RegistrationScreenItem>,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing_09),
        verticalArrangement = Arrangement.spacedBy(Spacing_08, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            items = content,
            itemContent = { item ->
                when (item) {
                    is RegistrationScreenItem.Text -> Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.prompt_screen_message),
                    )

                    is RegistrationScreenItem.Title -> Text(
                        modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
                        text = stringResource(R.string.prompt_screen_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )

                    RegistrationScreenItem.RegisterButton -> OutlinedButton(
                        onClick = { onAction(Action.OnRegisterButtonClick) },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        content = { Text(text = stringResource(R.string.register_button_label)) },
                    )

                    RegistrationScreenItem.SignInButton -> TextButton(
                        onClick = { onAction(Action.OnSignInButtonClick) },
                    ) {
                        Text(text = stringResource(R.string.sign_in_button_label))
                    }

                    RegistrationScreenItem.ProvidersDivider -> ProvidersDivider()
                    RegistrationScreenItem.Providers -> GoogleProviderButton(onAction)
                }
            },
        )
    }
}

@Composable
fun ProvidersDivider() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Spacing_08, bottom = Spacing_04),
        horizontalArrangement = Arrangement.spacedBy(Spacing_06),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(
            Modifier
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurfaceVariant)
                .weight(1f)
        )
        Text(text = "ó bien")
        Spacer(
            Modifier
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurfaceVariant)
                .weight(1f)
        )
    }
}

@Composable
private fun GoogleProviderButton(
    onAction: (Action) -> Unit,
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        shape = RoundedCornerShape(6.dp),
        onClick = { onAction(Action.OnProviderAuthButtonClick(AuthProvider.Gmail)) },
        content = {
            Image(
                painter = painterResource(R.drawable.google_logo),
                contentDescription = "",
            )
            Text(modifier = Modifier.padding(start = Spacing_06), text = "Continuar con Google")
        },
    )
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<PromptScreenViewModel.SideEffect>,
    navigateToEmailRegistration: () -> Unit,
    navigateToEmailSignIn: () -> Unit,
    navigateToOriginDestination: () -> Unit,
    providerSignInActivityLauncher: ActivityResultLauncher<Intent>,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                PromptScreenViewModel.SideEffect.NavigateToEmailRegistration -> {
                    navigateToEmailRegistration()
                }

                PromptScreenViewModel.SideEffect.NavigateToEmailSignIn -> {
                    navigateToEmailSignIn()
                }

                PromptScreenViewModel.SideEffect.NavigateToOriginDestination -> {
                    navigateToOriginDestination()
                }

                is PromptScreenViewModel.SideEffect.LaunchProviderActivity -> {
                    try {
                        providerSignInActivityLauncher.launch(sideEffect.intent)
                    } catch (e: Throwable) {
                        Log.e(TAG, e.stackTraceToString())
                    }
                }
            }
        }.collect()
    }
}