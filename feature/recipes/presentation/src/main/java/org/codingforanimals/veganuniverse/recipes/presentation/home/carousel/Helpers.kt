package org.codingforanimals.veganuniverse.recipes.presentation.home.carousel

import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

internal fun Modifier.cardShapeAndShadow(): Modifier {
    return shadow(elevation = 10.dp, shape = ShapeDefaults.Small)
}

@Composable
internal fun cardWidth(): Double {
    val view = LocalView.current
    return view.width / 2.7
}