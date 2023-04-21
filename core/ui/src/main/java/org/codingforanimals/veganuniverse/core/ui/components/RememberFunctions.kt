package org.codingforanimals.veganuniverse.core.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.core.content.ContextCompat

@Composable
fun rememberBitmap(resId: Int, size: DpSize? = null, tint: Color? = null): Bitmap {
    val context = LocalContext.current
    val density = LocalDensity.current
    return rememberSaveable {
        ContextCompat.getDrawable(context, resId)?.let { drawable ->
            val (widthPx, heightPx) = with(density) {
                Pair(
                    size?.width?.roundToPx() ?: drawable.intrinsicWidth,
                    size?.height?.roundToPx() ?: drawable.intrinsicHeight
                )
            }
            drawable.setBounds(0, 0, widthPx, heightPx)
            tint?.let { drawable.setTint(tint.toArgb()) }
            val bitmap = Bitmap.createBitmap(
                widthPx,
                heightPx,
                Bitmap.Config.ARGB_8888,
            )
            val canvas = Canvas(bitmap)
            drawable.draw(canvas)
            bitmap
        } ?: Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888)
    }
}