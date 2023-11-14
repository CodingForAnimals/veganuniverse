package org.codingforanimals.veganuniverse.places.presentation.details.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

@Composable
internal fun DiscardReviewDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = { Text(text = "¿Quieres descartar esta reseña?") },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(text = "Descartar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Volver")
            }
        },
        icon = { VUIcon(icon = VUIcons.Delete, contentDescription = "") },
        containerColor = MaterialTheme.colorScheme.surface,
        iconContentColor = MaterialTheme.colorScheme.onSurface,
    )
}