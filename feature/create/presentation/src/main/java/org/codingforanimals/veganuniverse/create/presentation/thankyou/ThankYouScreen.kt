package org.codingforanimals.veganuniverse.create.presentation.thankyou

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.VeganUniverseTheme
import org.codingforanimals.veganuniverse.ui.components.VeganUniverseBackground

@Composable
internal fun ThankYouScreen(
    navigateToCreateScreen: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing_06),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing_06, Alignment.CenterVertically),
    ) {
        Text(
            text = "¡Gracias por tu aporte!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(text = "En comunidad hacemos que volverse vegan sea más fácil y accesible cada día!")
        OutlinedButton(
            onClick = navigateToCreateScreen,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Continuar")
        }
    }
}

@Preview
@Composable
private fun PreviewThankYouScreen() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            ThankYouScreen {

            }
        }
    }
}