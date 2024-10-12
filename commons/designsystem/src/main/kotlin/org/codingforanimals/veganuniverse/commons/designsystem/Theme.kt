package org.codingforanimals.veganuniverse.commons.designsystem

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun VeganUniverseTheme(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = {
            val appScope = rememberCoroutineScope()
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.outline) { }
            Surface(
                modifier = modifier,
                color = Color(0xFFF0F0F1),
                content = content,
            )
        }
    )
}