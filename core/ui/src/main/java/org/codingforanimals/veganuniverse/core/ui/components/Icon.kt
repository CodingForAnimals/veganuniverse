package org.codingforanimals.veganuniverse.core.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.codingforanimals.veganuniverse.core.ui.icons.Icon

@Composable
fun VUIcon(
    modifier: Modifier = Modifier,
    icon: Icon,
    contentDescription: String,
) {
    when (icon) {
        is Icon.ImageVectorIcon -> {
            Icon(
                modifier = modifier,
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
            )
        }
        is Icon.DrawableResourceIcon -> {
            Icon(
                modifier = modifier,
                painter = painterResource(icon.id),
                contentDescription = contentDescription,
            )
        }
    }
}