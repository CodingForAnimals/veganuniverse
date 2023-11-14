@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.recipes.presentation.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.components.VUTag
import org.codingforanimals.veganuniverse.ui.components.VUTagDefaults
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

@Composable
internal fun RecipeReportDialog(
    onCloseDialog: () -> Unit,
) {
    Surface(
        shape = ShapeDefaults.Medium,
    ) {
        Column {
            Row {
                Spacer(modifier = Modifier.weight(1f))
                VUIcon(
                    icon = VUIcons.Close,
                    contentDescription = "",
                    onIconClick = onCloseDialog,
                )
            }
            Text(
                modifier = Modifier.padding(horizontal = Spacing_06),
                style = MaterialTheme.typography.bodyMedium,
                text = "Gracias por denunciar cosas que infringen las normas. Contanos qué ocurre y lo investigaremos",
            )
            var buttonEnabled by remember { mutableStateOf(false) }
            FlowRow(
                modifier = Modifier.padding(horizontal = Spacing_06, vertical = Spacing_04),
                horizontalArrangement = Arrangement.spacedBy(Spacing_04),
            ) {
                reportTags.forEach { tag ->
                    var selected by remember { mutableStateOf(false) }
                    val colors =
                        if (selected) VUTagDefaults.invertedTagColors() else VUTagDefaults.tagColors()
                    VUTag(
                        label = tag,
                        onClick = {
                            buttonEnabled = true
                            selected = !selected
                        },
                        colors = colors
                    )
                }
            }
            Row(
                Modifier.padding(end = Spacing_06, bottom = Spacing_04)
            ) {
                val buttonColors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    enabled = buttonEnabled,
                    colors = buttonColors,
                    onClick = { if (buttonEnabled) onCloseDialog() },
                    content = {
                        Text(text = "Reportar")
                    },
                )
            }
        }
    }
}

private val reportTags = listOf(
    "Receta no vegana",
    "Contenido inapropiado",
    "Información falsa",
    "Otros",
)