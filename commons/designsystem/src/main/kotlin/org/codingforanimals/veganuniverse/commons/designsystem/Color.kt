package org.codingforanimals.veganuniverse.commons.designsystem

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF3B0F8A)
val PrimaryLight = Color(0xFF7132E4)
val OnPrimary = Color(0xFFF4F4F5)
val Background = Color(0xFFE4E4E7)
val PrimaryContainer = Color(0xFFBABFC4)
val SecondaryContainer = Color(0xFFA1A1AA)
val Surface = Color(0xFFDFE1E3)
val SurfaceVariant = Color(0xFFF4F4F5)
val OnSurfaceVariant = Color(0xFF4D4D55)
val Secondary = Color(0xFFE0D2F9)
val Outline = Color(0xFFBABFC4)
val ShimmerBackground = Color(0xFF8F8F8F)

internal val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    secondary = Secondary,
    secondaryContainer = SecondaryContainer,
    surface = Surface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    background = Background,
    outline = Outline,
)