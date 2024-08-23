package org.codingforanimals.veganuniverse.commons.ui.contribution

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.ui.R

sealed class ReportContentDialogResult {
    data object Dismiss : ReportContentDialogResult()
    data object SendReport : ReportContentDialogResult()
}

@Composable
fun ReportContentDialog(
    onResult: (ReportContentDialogResult) -> Unit,
) {
    var userActionEnabled by remember { mutableStateOf(true) }
    AlertDialog(
        title = { Text(stringResource(R.string.report_content)) },
        text = { Text(text = stringResource(id = R.string.report_content_message)) },
        onDismissRequest = { onResult(ReportContentDialogResult.Dismiss) },
        confirmButton = {
            TextButton(
                onClick = {
                    userActionEnabled = false
                    onResult(ReportContentDialogResult.SendReport)
                },
                enabled = userActionEnabled,
                content = { Text(text = stringResource(R.string.report)) }
            )
        },
    )
}

sealed class EditContentDialogResult {
    data object Dismiss : EditContentDialogResult()
    data class SendEdit(val edition: String) : EditContentDialogResult()
}

@Composable
fun EditContentDialog(
    onResult: (EditContentDialogResult) -> Unit,
) {
    var edition: String? by remember { mutableStateOf(null) }
    val suggestionText by remember(edition) {
        derivedStateOf { edition ?: "" }
    }
    val error: Boolean by remember(edition) {
        derivedStateOf { edition?.isBlank() == true }
    }
    val buttonEnabled: Boolean = remember(edition) {
        edition?.isNotBlank() == true
    }
    var userActionEnabled by remember { mutableStateOf(true) }
    val focusRequester = LocalFocusManager.current
    AlertDialog(
        title = { Text(stringResource(R.string.send_suggestion)) },
        text = {
            Column {
                Text(text = stringResource(R.string.edit_dialog_message))
                TextField(
                    modifier = Modifier.padding(top = Spacing_04),
                    value = suggestionText,
                    onValueChange = { edition = it },
                    placeholder = { Text(text = stringResource(id = R.string.edit_dialog_edit_placeholder)) },
                    isError = error,
                    enabled = userActionEnabled,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusRequester.clearFocus() }
                    ),
                    supportingText = {
                        Text(stringResource(R.string.edit_input_required)).takeIf { error }
                    }
                )
            }
        },
        onDismissRequest = { onResult(EditContentDialogResult.Dismiss) },
        confirmButton = {
            TextButton(
                enabled = buttonEnabled && userActionEnabled,
                onClick = {
                    userActionEnabled = false
                    edition?.let { onResult(EditContentDialogResult.SendEdit(it)) }
                },
                content = { Text(text = stringResource(R.string.report)) }
            )
        }
    )
}
