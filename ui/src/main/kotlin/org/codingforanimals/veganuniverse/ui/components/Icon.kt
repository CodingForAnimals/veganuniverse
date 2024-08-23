package org.codingforanimals.veganuniverse.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.ui.icon.Icon


object VUIconDefaults {
    val defaultIconSize = 20.dp
}

@Composable
fun VUIcon(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    icon: Icon,
    onIconClick: () -> Unit,
    contentDescription: String? = null,
    tint: Color = Color.Unspecified,
) {
    IconButton(
        modifier = modifier,
        onClick = onIconClick,
        content = {
            VUIcon(
                modifier = iconModifier.size(VUIconDefaults.defaultIconSize),
                icon = icon,
                contentDescription = contentDescription,
                tint = tint
            )
        },
    )
}

@Composable
fun VUIcon(
    modifier: Modifier = Modifier,
    icon: Icon,
    contentDescription: String? = null,
) {
    when (icon) {
        is Icon.ImageVectorIcon -> {
            Icon(
                modifier = modifier.size(VUIconDefaults.defaultIconSize),
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
            )
        }

        is Icon.DrawableResourceIcon -> {
            Icon(
                modifier = modifier.size(VUIconDefaults.defaultIconSize),
                painter = painterResource(icon.id),
                contentDescription = contentDescription,
            )
        }

        is Icon.ImageDrawableResourceIcon -> {
            Image(
                modifier = modifier.size(VUIconDefaults.defaultIconSize),
                painter = painterResource(icon.id),
                contentDescription = contentDescription,
            )
        }
    }
}

@Composable
fun VUIcon(
    modifier: Modifier = Modifier,
    icon: Icon,
    tint: Color,
    contentDescription: String? = null,
) {
    when (icon) {
        is Icon.ImageVectorIcon -> {
            Icon(
                modifier = modifier.size(VUIconDefaults.defaultIconSize),
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                tint = tint,
            )
        }

        is Icon.DrawableResourceIcon -> {
            Icon(
                modifier = modifier.size(VUIconDefaults.defaultIconSize),
                painter = painterResource(icon.id),
                contentDescription = contentDescription,
                tint = tint,
            )
        }

        is Icon.ImageDrawableResourceIcon -> {
            Image(
                modifier = modifier.size(VUIconDefaults.defaultIconSize),
                painter = painterResource(icon.id),
                contentDescription = contentDescription,
            )
        }
    }
}
