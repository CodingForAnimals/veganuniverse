package org.codingforanimals.veganuniverse.commons.ui.icon

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.components.VUIconDefaults

data class ToggleIconState(
    val loading: Boolean = true,
    val value: Boolean = false,
)

@Composable
fun ToggleableIcon(
    modifier: Modifier = Modifier,
    state: ToggleIconState = ToggleIconState(),
    onIconClick: () -> Unit,
    onIcon: Icon,
    onTint: Color = Color.Unspecified,
    offIcon: Icon,
    offTint: Color = Color.Unspecified,
) {
    val icon = remember(state.value) {
        if (state.value) {
            onIcon
        } else {
            offIcon
        }
    }

    val tint = remember(state.value) {
        if (state.value) {
            onTint
        } else {
            offTint
        }
    }

    Crossfade(
        modifier = modifier,
        targetState = state.loading,
        label = "toggleable_icon_crossfade_animation"
    ) {
        if (it) {
            IconButton(onClick = {}, enabled = false) {
                CircularProgressIndicator(
                    modifier = Modifier.size(VUIconDefaults.defaultIconSize),
                    strokeWidth = 2.dp,
                )
            }
        } else {
            VUIcon(
                icon = icon,
                contentDescription = null,
                onIconClick = onIconClick,
                tint = tint,
            )
        }
    }
}