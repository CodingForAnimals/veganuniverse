package org.codingforanimals.veganuniverse.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import org.codingforanimals.veganuniverse.core.ui.icons.Image

@Composable
fun VUImage(
    modifier: Modifier = Modifier,
    image: Image,
) {
    val model = when (image) {
        is Image.DrawableResourceImage -> image.resId
    }
    AsyncImage(
        modifier = modifier,
        model = model,
        contentDescription = "",
    )
}