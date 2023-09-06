package org.codingforanimals.veganuniverse.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun VerifyEmailPromptScreen(
    onSendRequest: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Para continuar, necesitamos que verifiques tu email",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = "Por cuestiones de seguridad solo usuarios con email verificados o que hayan iniciado sesión con Google pueden acceder a ciertas funcionalidades de la app")
                Button(onClick = onSendRequest) {
                    Text(text = "Enviar email de verificación")
                }
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Volver")
                }
            }
        }
    }
}