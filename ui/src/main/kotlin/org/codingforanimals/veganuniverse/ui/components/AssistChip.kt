package org.codingforanimals.veganuniverse.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.codingforanimals.veganuniverse.ui.Spacing_01
import org.codingforanimals.veganuniverse.ui.VeganUniverseTheme
import org.codingforanimals.veganuniverse.ui.icon.Icon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

@Composable
fun VUAssistChip(
    modifier: Modifier = Modifier,
    icon: Icon? = null,
    label: String? = null,
    onClick: () -> Unit,
    iconDescription: String,
    colors: ChipColors = VUAssistChipDefaults.primaryColors(),
) {
    AssistChip(
        modifier = modifier,
        leadingIcon = {
            icon?.let {
                VUIcon(
                    modifier = Modifier.padding(start = Spacing_01),
                    icon = icon,
                    contentDescription = iconDescription,
                )
            }
        },
        border = null,
        colors = colors,
        onClick = onClick,
        shape = CircleShape,
        interactionSource = MutableInteractionSource(),
        label = {
            label?.let {
                Text(label)
            }
        }
    )
}

object VUAssistChipDefaults {
    @Composable
    fun primarySelectedColors() = AssistChipDefaults.assistChipColors(
        containerColor = MaterialTheme.colorScheme.primary,
        labelColor = MaterialTheme.colorScheme.onPrimary,
        leadingIconContentColor = MaterialTheme.colorScheme.onPrimary,
    )

    @Composable
    fun primaryColors() = AssistChipDefaults.assistChipColors(
        labelColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    )

    @Composable
    fun secondaryColors() = AssistChipDefaults.assistChipColors(
        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        leadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
@Preview
private fun PreviewVUAssistChip() {
    VeganUniverseTheme {
        Column {
            VUAssistChip(
                label = "Assist chip",
                onClick = {},
                iconDescription = "",
            )
            VUAssistChip(
                label = "Assist chip",
                icon = VUIcons.Filter,
                onClick = {},
                iconDescription = "",
            )
            VUAssistChip(
                label = "Assist chip",
                icon = VUIcons.Filter,
                onClick = {},
                iconDescription = "",
                colors = VUAssistChipDefaults.primarySelectedColors(),
            )
            VUAssistChip(
                label = "Assist chip",
                icon = VUIcons.Filter,
                onClick = {},
                iconDescription = "",
                colors = VUAssistChipDefaults.secondaryColors(),
            )
            VUAssistChip(
                label = "Assist chip",
                icon = VUIcons.Filter,
                onClick = {},
                iconDescription = "",
                colors = VUAssistChipDefaults.primaryColors(),
            )
        }
    }
}