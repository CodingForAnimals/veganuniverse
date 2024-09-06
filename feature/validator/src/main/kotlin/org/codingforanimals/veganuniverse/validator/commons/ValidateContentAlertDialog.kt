package org.codingforanimals.veganuniverse.validator.commons

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.validator.R

@Composable
internal fun ValidateContentAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(stringResource(R.string.validate_content_title))
        },
        text = {
            Text(stringResource(R.string.validate_content_text))
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                    onConfirm()
                },
                content = {
                    Text(stringResource(R.string.confirm))
                }
            )
        }
    )
}