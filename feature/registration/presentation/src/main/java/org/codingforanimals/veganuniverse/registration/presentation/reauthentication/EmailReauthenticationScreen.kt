@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.registration.presentation.reauthentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.commons.ui.snackbar.Snackbar
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.ReauthenticationUseCases
import org.codingforanimals.veganuniverse.registration.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun EmailReauthenticationScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
) {
    val viewModel: EmailReauthenticationViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    when (state) {
        EmailReauthenticationViewModel.State.Loading -> Unit
        EmailReauthenticationViewModel.State.Content -> {
            EmailReauthenticationScreen(
                password = viewModel.password,
                isError = viewModel.isError,
                modifier = modifier,
                snackbarHostState = snackbarHostState,
                navigateUp = navigateUp,
                onPasswordChange = viewModel::onPasswordChange,
                reauthenticate = viewModel::reauthenticate,
            )

            VUCircularProgressIndicator(
                visible = viewModel.loading
            )
        }
    }
    HandleSnackbarEffects(
        snackbarEffects = viewModel.snackbarEffects,
        snackbarHostState = snackbarHostState,
    )
    LaunchedEffect(Unit) {
        viewModel.result.onEach { navigateUp() }.collect()
    }
}

internal class EmailReauthenticationViewModel(
    private val reauthenticationUseCases: ReauthenticationUseCases,
) : ViewModel() {

    private val resultChannel = Channel<Unit>()
    val result = resultChannel.receiveAsFlow()

    val state = flow {
        when (reauthenticationUseCases.reauthenticate()){
            ReauthenticationUseCases.Result.UnexpectedError,
            ReauthenticationUseCases.Result.GmailReauthenticationSuccess -> {
                resultChannel.send(Unit)
            }
            ReauthenticationUseCases.Result.UserMustEnterPassword -> {
                emit(State.Content)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State.Loading,
    )

    sealed class State {
        data object Loading : State()
        data object Content : State()
    }

    private val snackbarEffectsChannel = Channel<Snackbar>()
    val snackbarEffects = snackbarEffectsChannel.receiveAsFlow()

    var loading: Boolean by mutableStateOf(false)
        private set

    var isError by mutableStateOf(false)
        private set

    var password by mutableStateOf("")
        private set

    fun reauthenticate() {
        viewModelScope.launch {
            loading = true
            val result = reauthenticationUseCases.emailReauthentication(password)
            loading = false
            if (result.isSuccess) {
                resultChannel.send(Unit)
            } else {
                isError = true
                snackbarEffectsChannel.send(
                    Snackbar(
                        message = R.string.reauthentication_failed,
                        duration = SnackbarDuration.Long,
                    )
                )
            }
        }
    }

    fun onPasswordChange(input: String) {
        isError = false
        password = input
    }
}

@Composable
private fun EmailReauthenticationScreen(
    password: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navigateUp: () -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    reauthenticate: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = navigateUp,
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                            )
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = Spacing_04),
            ) {
                Text(
                    text = "¡Felicitaciones!",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    modifier = Modifier.padding(top = Spacing_06),
                    text = "Tu correo electrónico fue validado exitósamente",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    modifier = Modifier.padding(top = Spacing_06),
                    text = "¡Estás a un paso de poder contribuir en Universo Vegano! Por cuestiones de seguridad, necesitamos verificar tu contraseña una última vez.",
                    style = MaterialTheme.typography.bodyMedium,
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing_06),
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholder = { Text(text = "Ingresá tu contraseña") },
                    leadingIcon = {
                        Icon(
                            painterResource(VUIcons.Lock.id),
                            contentDescription = null
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        autoCorrect = false,
                        imeAction = ImeAction.Go,
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = { reauthenticate() },
                    ),
                    maxLines = 1,
                    isError = isError,
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spacing_06),
                    enabled = password.length >= 6 && !isError,
                    onClick = reauthenticate,
                    content = {
                        Text(text = stringResource(R.string.continue_label))
                    }
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewEmailReauthenticationScreen() {
    VeganUniverseTheme {
        EmailReauthenticationScreen(
            password = "123123",
            isError = false,
        )
    }
}

@Preview
@Composable
private fun PreviewErrorEmailReauthenticationScreen() {
    VeganUniverseTheme {
        EmailReauthenticationScreen(
            password = "123123",
            isError = true,
        )
    }
}