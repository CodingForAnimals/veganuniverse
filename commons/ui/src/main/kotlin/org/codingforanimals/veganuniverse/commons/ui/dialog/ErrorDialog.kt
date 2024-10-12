package org.codingforanimals.veganuniverse.commons.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.R

@Composable
fun ErrorDialog(
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(id = R.string.unexpected_error_title))
        },
        text = {
            Text(text = stringResource(id = R.string.unexpected_error_message))
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.back))
            }
        }
    )
}

@Preview
@Composable
private fun PreviewErrorDialog() {
    VeganUniverseTheme {
        ErrorDialog(
            onDismissRequest = {},
        )
    }
}
