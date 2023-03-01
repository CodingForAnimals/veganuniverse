package org.codingforanimals.veganuniverse.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.codingforanimals.veganuniverse.core.ui.R

val Poppins = FontFamily(
    Font(resId = R.font.poppins_thin, weight = FontWeight.Thin, style = FontStyle.Normal),
    Font(resId = R.font.poppins_thin_italic, weight = FontWeight.Thin, style = FontStyle.Italic),
    Font(
        resId = R.font.poppins_extra_light,
        weight = FontWeight.ExtraLight,
        style = FontStyle.Normal
    ),
    Font(
        resId = R.font.poppins_extra_light_italic,
        weight = FontWeight.ExtraLight,
        style = FontStyle.Italic
    ),
    Font(resId = R.font.poppins_light, weight = FontWeight.Light, style = FontStyle.Normal),
    Font(resId = R.font.poppins_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(resId = R.font.poppins_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(resId = R.font.poppins_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(
        resId = R.font.poppins_medium_italic,
        weight = FontWeight.Medium,
        style = FontStyle.Italic
    ),
    Font(resId = R.font.poppins_semi_bold, weight = FontWeight.SemiBold, style = FontStyle.Normal),
    Font(
        resId = R.font.poppins_semi_bold_italic,
        weight = FontWeight.SemiBold,
        style = FontStyle.Italic
    ),
    Font(resId = R.font.poppins_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(resId = R.font.poppins_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(
        resId = R.font.poppins_extra_bold,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Normal
    ),
    Font(
        resId = R.font.poppins_extra_bold_italic,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Italic
    ),
    Font(resId = R.font.poppins_black, weight = FontWeight.Black, style = FontStyle.Normal),
    Font(resId = R.font.poppins_black_italic, weight = FontWeight.Black, style = FontStyle.Italic),
)
val Typography = Typography(
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
).defaultFontFamily(fontFamily = Poppins)

private fun Typography.defaultFontFamily(fontFamily: FontFamily): Typography {
    return copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}