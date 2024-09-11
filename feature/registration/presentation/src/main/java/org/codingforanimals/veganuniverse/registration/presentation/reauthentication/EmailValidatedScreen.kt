@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.registration.presentation.reauthentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_10
import org.codingforanimals.veganuniverse.commons.designsystem.VeganUniverseTheme
import org.codingforanimals.veganuniverse.commons.ui.R.string.back
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.registration.presentation.R

@Composable
internal fun EmailValidatedScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    navigateToEmailSignIn: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = navigateUp,
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = stringResource(back),
                            )
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = Spacing_04),
            ) {
                Text(
                    text = "¡Felicitaciones!",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    modifier = Modifier.padding(top = Spacing_06),
                    text = "Tu correo electrónico fue validado exitósamente",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    modifier = Modifier.padding(top = Spacing_06),
                    text = "¡Estás a un paso de poder contribuir en Universo Vegano! Por cuestiones de seguridad, necesitamos que inicies sesión nuevamente.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Icon(
                    modifier = Modifier
                        .padding(vertical = Spacing_10)
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(id = VUIcons.Email.id),
                    contentDescription = null,
                )
                Button(
                    modifier = Modifier
                        .padding(top = Spacing_06)
                        .align(Alignment.CenterHorizontally),
                    onClick = navigateToEmailSignIn,
                    content = {
                        Text(text = stringResource(R.string.sign_in_again))
                    }
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewEmailReauthenticationScreen() {
    VeganUniverseTheme {
        EmailValidatedScreen()
    }
}
