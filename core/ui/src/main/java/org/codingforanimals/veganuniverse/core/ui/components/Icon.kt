package org.codingforanimals.veganuniverse.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.icons.Icon

@Composable
fun VUIcon(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    icon: Icon,
    onIconClick: () -> Unit,
    contentDescription: String,
) {
    IconButton(
        modifier = modifier,
        onClick = onIconClick,
        content = {
            VUIcon(
                modifier = iconModifier.size(defaultIconSize),
                icon = icon,
                contentDescription = contentDescription
            )
        },
    )
}

@Composable
fun VUIcon(
    modifier: Modifier = Modifier,
    icon: Icon,
    contentDescription: String,
    tint: Color = Color.Unspecified,
) {
    when (icon) {
        is Icon.ImageVectorIcon -> {
            Icon(
                modifier = modifier.size(defaultIconSize),
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                tint = tint,
            )
        }
        is Icon.DrawableResourceIcon -> {
            Icon(
                modifier = modifier.size(defaultIconSize),
                painter = painterResource(icon.id),
                contentDescription = contentDescription,
                tint = tint,
            )
        }
    }
}

private val defaultIconSize = 19.dp