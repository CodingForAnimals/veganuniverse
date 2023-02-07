package org.codingforanimals.veganuniverse.registration.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import org.codingforanimals.veganuniverse.core.ui.R

@Composable
fun WelcomeScreen(
    onLoginSuccess: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.universe_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(onClick = onLoginSuccess) {
                Text(text = "Registrarse")
            }

            TextButton(onClick = onLoginSuccess) {
                Text(text = "Ingresar como visitante")
            }
        }
    }
}