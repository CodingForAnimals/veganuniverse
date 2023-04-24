package org.codingforanimals.veganuniverse.create.presentation.place.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
internal fun TryAgainAlertDialog(
    dismissDialog: () -> Unit,
    tryAgain: () -> Unit,
) {
    AlertDialog(
        title = { Text(text = "Oops, algo salió mal...") },
        text = { Text(text = "No hemos logrado publicar este lugar. Puedes volver a intentarlo") },
        onDismissRequest = dismissDialog,
        confirmButton = {
            TextButton(
                content = { Text(text = "Reintentar") },
                onClick = tryAgain,
            )
        },
        dismissButton = {
            TextButton(
                content = { Text(text = "Atrás") },
                onClick = dismissDialog,
            )
        },
    )
}