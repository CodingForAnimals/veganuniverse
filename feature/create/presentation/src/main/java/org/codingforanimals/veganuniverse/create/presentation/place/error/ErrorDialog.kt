package org.codingforanimals.veganuniverse.create.presentation.place.error

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.create.presentation.R

@Composable
internal fun ErrorDialog(
    errorData: CreatePlaceErrorDialog,
    navigateToAlreadyExistingPlace: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val dismissButton: (@Composable () -> Unit)? =
        if (errorData is CreatePlaceErrorDialog.PlaceAlreadyExists) {
            {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Volver")
                }
            }
        } else {
            null
        }
    val confirmButton: (@Composable () -> Unit) =
        if (errorData is CreatePlaceErrorDialog.PlaceAlreadyExists) {
            {
                Button(onClick = navigateToAlreadyExistingPlace) {
                    Text(text = "Ver lugar")
                }
            }
        } else {
            {
                TextButton(onClick = onDismissRequest) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(errorData.title)) },
        text = { Text(text = stringResource(errorData.message)) },
        confirmButton = confirmButton,
        dismissButton = dismissButton,
    )
}