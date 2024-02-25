package org.codingforanimals.veganuniverse.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    iconDescription: String? = null,
    chipElevation: ChipElevation = AssistChipDefaults.assistChipElevation(),
    colors: ChipColors = VUAssistChipDefaults.primaryColors(),
    enabled: Boolean = true,
    borderStroke: BorderStroke? = null,
) {
    AssistChip(
        modifier = modifier,
        enabled = enabled,
        leadingIcon = {
            icon?.let {
                VUIcon(
                    modifier = Modifier.padding(start = Spacing_01),
                    icon = icon,
                    contentDescription = iconDescription,
                )
            }
        },
        border = borderStroke,
        colors = colors,
        onClick = onClick,
        shape = CircleShape,
        label = {
            label?.let {
                Text(label)
            }
        },
        elevation = chipElevation
    )
}

object VUAssistChipDefaults {
    @Composable
    fun elevatedAssistChipElevation() =
        AssistChipDefaults.elevatedAssistChipElevation(elevation = 3.dp)

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
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            VUAssistChip(
                icon = VUIcons.Filter,
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