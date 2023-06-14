package org.codingforanimals.veganuniverse.core.ui.place

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons

sealed class PlaceMarker(
    private val defaultIcon: Icon.DrawableResourceIcon,
    private val selectedIcon: Icon.DrawableResourceIcon,
) {
    @Composable
    fun getDisplayMarker(isSelected: Boolean): Bitmap? {
        val context = LocalContext.current
        return remember(key1 = isSelected) {
            val resId = if (isSelected) selectedIcon.id else defaultIcon.id
            createBitmap(context, resId)
        }
    }

    private fun createBitmap(context: Context, resId: Int): Bitmap? {
        return ContextCompat.getDrawable(context, resId)?.let { drawable ->
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888,
            )
            val canvas = Canvas(bitmap)
            drawable.draw(canvas)
            bitmap
        }
    }

    object RestaurantMarker : PlaceMarker(
        defaultIcon = VUIcons.MarkerRestaurant,
        selectedIcon = VUIcons.MarkerRestaurantSelected,
    )

    object StoreMarker : PlaceMarker(
        defaultIcon = VUIcons.MarkerStore,
        selectedIcon = VUIcons.MarkerStoreSelected,
    )

    object CafeMarker : PlaceMarker(
        defaultIcon = VUIcons.MarkerCafe,
        selectedIcon = VUIcons.MarkerCafeSelected,
    )

    object BarMarker : PlaceMarker(
        defaultIcon = VUIcons.Beer,
        selectedIcon = VUIcons.Beer,
    )

    object DefaultMarker : PlaceMarker(
        defaultIcon = VUIcons.LocationFilled,
        selectedIcon = VUIcons.LocationFilled,
    )
}
