@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.theme.VeganUniverseTheme

@Composable
fun VUTag(
    modifier: Modifier = Modifier,
    label: String,
    onClick: (() -> Unit)? = null,
    colors: VUTagColors = VUTagDefaults.tagColors()
) {
    SuggestionChip(
        modifier = modifier,
        onClick = { onClick?.invoke() },
        label = { Text(label) },
        enabled = onClick != null,
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = colors.containerColor,
            labelColor = colors.labelColor,
            disabledContainerColor = colors.containerColor,
            disabledLabelColor = colors.labelColor,
        ),
        border = SuggestionChipDefaults.suggestionChipBorder(disabledBorderColor = colors.borderColor)
    )
}

object VUTagDefaults {
    @Composable
    fun tagColors() = VUTagColors(
        containerColor = Color.Transparent,
        labelColor = MaterialTheme.colorScheme.primary,
        borderColor = MaterialTheme.colorScheme.primary,
    )

    @Composable
    fun invertedTagColors() = VUTagColors(
        containerColor = MaterialTheme.colorScheme.primary,
        labelColor = MaterialTheme.colorScheme.onPrimary,
        borderColor = MaterialTheme.colorScheme.primary,
    )
}

data class VUTagColors(
    val containerColor: Color,
    val labelColor: Color,
    val borderColor: Color,
)

@Preview
@Composable
private fun PreviewTag() {
    VeganUniverseTheme {
        VeganUniverseBackground {
            Column {
                VUTag(label = "Tag 1", modifier = Modifier.padding(16.dp))
                VUTag(label = "Tag 2", modifier = Modifier.padding(16.dp))
            }
        }
    }
}