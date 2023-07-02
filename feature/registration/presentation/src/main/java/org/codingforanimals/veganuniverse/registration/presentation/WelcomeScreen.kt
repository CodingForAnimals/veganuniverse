package org.codingforanimals.veganuniverse.registration.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.R.drawable.logo
import org.codingforanimals.veganuniverse.core.ui.R.drawable.universe_background

//TODO delete
@Composable
internal fun WelcomeScreen(
    onLoginSuccess: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(universe_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(
                modifier = Modifier
                    .padding(top = 54.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                Image(
                    modifier = Modifier.height(75.dp),
                    painter = painterResource(logo), contentDescription = "Universo Vegano, logo"
                )

                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        color = Color.White,
                        text = "Universo",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        color = Color.White,
                        text = "Vegano",
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))


            VeganUniverseAuth(onLoginSuccess)

            SocialMediaAuth()
        }
    }
}

@Composable
private fun VeganUniverseAuth(
    onLoginSuccess: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
    ) {
        Button(onClick = onLoginSuccess) {
            Text(text = "Iniciar sesión")
        }

        TextButton(
            onClick = onLoginSuccess,
            border = BorderStroke(1.dp, Color.White)
        ) {
            Text(
                text = "Ingresar como visitante",
                color = Color.White,
            )
        }
    }
}

@Composable
private fun SocialMediaAuth() {
    Column(
        modifier = Modifier.padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = "Continuar con",
            color = Color.White,
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
//            SocialMediaButton(
//                iconResId = R.drawable.ic_facebook,
//                contentDescription = "Inicia sesión con Facebook"
//            )
//            SocialMediaButton(
//                iconResId = R.drawable.ic_google,
//                contentDescription = "Inicia sesión con Google"
//            )
        }
    }
}

@Composable
private fun SocialMediaButton(
    iconResId: Int,
    contentDescription: String,
) {
    TextButton(
        modifier = Modifier
            .size(45.dp)
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        onClick = {}) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = contentDescription,
            tint = Color.Black
        )
    }
}