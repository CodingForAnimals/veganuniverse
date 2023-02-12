package org.codingforanimals.veganuniverse.core.ui.icons

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Place
import androidx.compose.ui.graphics.vector.ImageVector

object VeganUniverseIcons {
    val Community = Icons.Outlined.Forum
    val Places = Icons.Outlined.Place
    val Recipes = Icons.Default.Kitchen
    val Profile = Icons.Outlined.PersonOutline
    val Create = Icons.Outlined.AddBox
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector): Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int): Icon()
}