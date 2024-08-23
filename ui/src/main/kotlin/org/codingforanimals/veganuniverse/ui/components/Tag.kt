package org.codingforanimals.veganuniverse.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.ui.VeganUniverseTheme
import org.codingforanimals.veganuniverse.ui.icon.Icon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

@Composable
fun SelectableChip(
    modifier: Modifier = Modifier,
    label: String,
    icon: Icon? = null,
    selected: Boolean,
    selectedIcon: Icon = VUIcons.Check,
    shape: Shape = AssistChipDefaults.shape,
    onClick: () -> Unit,
) {
    val color = when {
        selected -> VUSelectableChipDefaults.selectedColors()
        else -> VUSelectableChipDefaults.idleColors()
    }
    val containerColor = animateColorAsState(
        targetValue = color.containerColor,
        label = "${label}_container_color_animation",
    )
    val borderColor = animateColorAsState(
        targetValue = color.borderColor,
        label = "${label}_border_color_animation",
    )

    AssistChip(
        modifier = modifier,
        shape = shape,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = {
            icon?.let {
                Crossfade(targetState = selected, label = "${label}_crossfade_animation") {
                    if (it) {
                        VUIcon(
                            icon = selectedIcon,
                            contentDescription = ""
                        )
                    } else {
                        VUIcon(
                            icon = icon,
                            contentDescription = "",
                        )
                    }
                }
            }
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = containerColor.value,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        border = AssistChipDefaults.assistChipBorder(
            enabled = true,
            borderColor = borderColor.value
        )
    )
}

object VUSelectableChipDefaults {
    @Composable
    fun selectedColors() = VUSelectableChipColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        borderColor = MaterialTheme.colorScheme.secondary,
    )

    @Composable
    fun idleColors() = VUSelectableChipColors(
        containerColor = Color.Transparent,
        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        borderColor = MaterialTheme.colorScheme.primary,
    )
}

data class VUSelectableChipColors(
    val containerColor: Color,
    val labelColor: Color,
    val borderColor: Color,
)

@Composable
fun VUTag(
    modifier: Modifier = Modifier,
    label: String,
    onClick: (() -> Unit)? = null,
    colors: VUTagColors = VUTagDefaults.tagColors(),
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
        border = SuggestionChipDefaults.suggestionChipBorder(
            enabled = true,
            disabledBorderColor = colors.borderColor
        )
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