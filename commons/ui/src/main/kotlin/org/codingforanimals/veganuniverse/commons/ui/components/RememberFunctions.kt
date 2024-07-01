package org.codingforanimals.veganuniverse.commons.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


@Composable
fun rememberBitmap(resId: Int, key: String? = null): Bitmap {
    val context = LocalContext.current
    return rememberSaveable(key = key) {
        ContextCompat.getDrawable(context, resId)?.let { drawable ->
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888,
            )
            val canvas = Canvas(bitmap)
            drawable.draw(canvas)
            bitmap
        } ?: Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888)
    }
}