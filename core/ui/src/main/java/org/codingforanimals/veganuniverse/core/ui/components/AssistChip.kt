@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.theme.Spacing_01

@Composable
fun VUAssistChip(
    modifier: Modifier = Modifier,
    icon: Icon,
    label: String?,
    onClick: () -> Unit,
    iconDescription: String,
    colors: ChipColors = VUAssistChipDefaults.assistChipColors(),
) {
    AssistChip(
        modifier = modifier,
        leadingIcon = {
            VUIcon(
                modifier = Modifier
                    .size(19.dp)
                    .padding(start = Spacing_01),
                icon = icon,
                contentDescription = iconDescription
            )
        },
        border = null,
        colors = colors,
        onClick = onClick,
        shape = CircleShape,
        interactionSource = MutableInteractionSource(),
        label = { label?.let { Text(it) } }
    )
}

object VUAssistChipDefaults {
    @Composable
    fun assistChipColors() = AssistChipDefaults.assistChipColors(
        labelColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    )

    @Composable
    fun secondaryAssistChipColors() = AssistChipDefaults.assistChipColors(
        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        leadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}