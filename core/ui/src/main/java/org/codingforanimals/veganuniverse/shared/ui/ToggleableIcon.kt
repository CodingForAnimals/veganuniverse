package org.codingforanimals.veganuniverse.shared.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VUIconDefaults
import org.codingforanimals.veganuniverse.core.ui.icons.Icon

@Composable
fun ToggleableIcon(
    modifier: Modifier = Modifier,
    toggled: Boolean,
    onIconClick: () -> Unit,
    onIcon: Icon,
    onTint: Color = Color.Unspecified,
    offIcon: Icon,
    offTint: Color = Color.Unspecified,
    loading: Boolean = false,
) {
    val icon = remember(toggled) {
        if (toggled) {
            onIcon
        } else {
            offIcon
        }
    }

    val tint = remember(toggled) {
        if (toggled) {
            onTint
        } else {
            offTint
        }
    }

    Crossfade(
        modifier = modifier,
        targetState = loading,
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
                contentDescription = stringResource(R.string.like),
                onIconClick = onIconClick,
                tint = tint,
            )
        }
    }
}