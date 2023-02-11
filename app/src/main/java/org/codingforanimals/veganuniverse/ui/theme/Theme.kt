package org.codingforanimals.veganuniverse.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import org.codingforanimals.veganuniverse.core.ui.theme.Green
import org.codingforanimals.veganuniverse.core.ui.theme.LightGreen
import org.codingforanimals.veganuniverse.core.ui.theme.LightGrey
import org.codingforanimals.veganuniverse.core.ui.theme.Pink40
import org.codingforanimals.veganuniverse.core.ui.theme.Pink80
import org.codingforanimals.veganuniverse.core.ui.theme.Purple80
import org.codingforanimals.veganuniverse.core.ui.theme.PurpleGrey40
import org.codingforanimals.veganuniverse.core.ui.theme.PurpleGrey80
import org.codingforanimals.veganuniverse.core.ui.theme.Typography

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Green,
    primaryContainer = LightGreen,
    background = LightGrey,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun VeganUniverseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}