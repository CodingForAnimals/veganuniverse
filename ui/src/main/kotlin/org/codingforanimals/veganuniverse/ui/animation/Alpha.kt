package org.codingforanimals.veganuniverse.ui.animation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha

fun Modifier.animateAlphaOnStart(): Modifier = composed {
    var animate by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animate = true
    }
    val alpha = animateFloatAsState(
        targetValue = if (!animate) 0f else 1f,
        label = "on_start_alpha_animation",
    )
    return@composed alpha(alpha.value)
}