package org.codingforanimals.veganuniverse.commons.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun VeganUniverseTheme(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = {
            Surface(
                modifier = modifier,
                color = Color(0xFFF0F0F1),
                content = content,
            )
        }
    )
}