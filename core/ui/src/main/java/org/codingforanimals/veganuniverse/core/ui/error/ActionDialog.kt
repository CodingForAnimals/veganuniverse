package org.codingforanimals.veganuniverse.core.ui.error

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


@Composable
fun ActionDialog(
    @StringRes title: Int,
    @StringRes message: Int,
    @StringRes confirmLabel: Int,
    @StringRes dismissLabel: Int,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(title)) },
        text = { Text(text = stringResource(message)) },
        confirmButton = {
            Button(onClick = onConfirmRequest) {
                Text(text = stringResource(confirmLabel))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(dismissLabel))
            }
        }
    )
}