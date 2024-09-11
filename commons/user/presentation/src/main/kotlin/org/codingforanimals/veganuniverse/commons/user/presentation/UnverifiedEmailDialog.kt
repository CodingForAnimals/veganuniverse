package org.codingforanimals.veganuniverse.commons.user.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.SendVerificationEmail
import org.koin.androidx.compose.koinViewModel

sealed class UnverifiedEmailResult {
    data object Dismissed : UnverifiedEmailResult()
    data object UnexpectedError : UnverifiedEmailResult()
    data object VerificationEmailSent : UnverifiedEmailResult()
}

@Composable
fun UnverifiedEmailDialog(
    modifier: Modifier = Modifier,
    onResult: (UnverifiedEmailResult) -> Unit,
) {
    val viewModel: UnverifiedEmailViewModel = koinViewModel()

    UnverifiedEmailDialog(
        modifier = modifier,
        onDismissRequest = { onResult(UnverifiedEmailResult.Dismissed) },
        onConfirmClick = viewModel::sendVerificationEmail,
        isLoading = viewModel.isLoading,
    )

    LaunchedEffect(Unit) {
        viewModel.result.onEach { result -> onResult(result) }.collect()
    }
}

@Composable
private fun UnverifiedEmailDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    isLoading: Boolean = false,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = onConfirmClick,
                enabled = !isLoading,
                content = { Text(text = stringResource(R.string.send)) }
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                content = { Text(text = stringResource(back)) }
            )
        },
        title = { Text(text = stringResource(id = R.string.unverified_email_dialog_title)) },
        text = { Text(text = stringResource(id = R.string.unverified_email_dialog_text)) }
    )
}

internal class UnverifiedEmailViewModel(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val useCase: SendVerificationEmail,
) : ViewModel() {
    var isLoading: Boolean by mutableStateOf(false)
        private set

    private val resultChannel = Channel<UnverifiedEmailResult>()
    val result = resultChannel.receiveAsFlow()

    fun sendVerificationEmail() {
        viewModelScope.launch {
            isLoading = true
            flowOnCurrentUser().firstOrNull()?.let {
                useCase().takeIf { it.isSuccess }?.let {
                    resultChannel.send(UnverifiedEmailResult.VerificationEmailSent)
                } ?: resultChannel.send(UnverifiedEmailResult.UnexpectedError)
            } ?: resultChannel.send(UnverifiedEmailResult.UnexpectedError)
        }.invokeOnCompletion { isLoading = false }
    }
}

@Preview
@Composable
private fun PreviewUnverifiedEmailDialog() {
    VeganUniverseTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            UnverifiedEmailDialog()
        }
    }
}
