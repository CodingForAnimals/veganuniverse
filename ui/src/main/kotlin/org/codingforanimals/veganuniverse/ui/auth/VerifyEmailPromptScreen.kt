package org.codingforanimals.veganuniverse.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.VeganUniverseTheme
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

@Composable
fun VerifyEmailPromptScreen(
    onSendRequest: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    modifier = Modifier.padding(Spacing_06),
                    onClick = onDismissRequest
                ) {
                    VUIcon(
                        icon = VUIcons.Close,
                        contentDescription = "",
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Spacing_06),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Para continuar, necesitamos que verifiques tu email",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(text = "Por cuestiones de seguridad solo usuarios con email verificados o que hayan iniciado sesión con Google pueden acceder a ciertas funcionalidades de la app")
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
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
    }
}

@Preview
@Composable
private fun PreviewVerifyEmailPromptScreen() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            VerifyEmailPromptScreen(
                onSendRequest = {},
                onDismissRequest = {},
            )
        }
    }
}