package org.codingforanimals.places.presentation.details.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.codingforanimals.places.presentation.R

@Composable
internal fun ErrorDialog(
    title: String,
    message: String,
    onConfirmButtonClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirmButtonClick) {
                Text(stringResource(R.string.error_dismiss_button_label))
            }
        },
    )
}