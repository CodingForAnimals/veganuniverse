package org.codingforanimals.places.presentation.home.composables

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons

object Markers {
    val restaurantMarker = PlaceMarker(
        defaultIcon = VUIcons.MarkerRestaurant,
        selectedIcon = VUIcons.MarkerRestaurantSelected,
    )

    val storeMarker = PlaceMarker(
        defaultIcon = VUIcons.MarkerStore,
        selectedIcon = VUIcons.MarkerStoreSelected,
    )

    val cafeMarker = PlaceMarker(
        defaultIcon = VUIcons.MarkerCafe,
        selectedIcon = VUIcons.MarkerCafeSelected,
    )
}

data class PlaceMarker(
    private val defaultIcon: Icon.DrawableResourceIcon,
    private val selectedIcon: Icon.DrawableResourceIcon,
) {

    @Composable
    fun getDisplayMarker(isSelected: Boolean): BitmapDescriptor {
        val context = LocalContext.current
        return remember(key1 = isSelected) {
            val resId = if (isSelected) selectedIcon.id else defaultIcon.id
            createBitmap(context, resId)?.let {
                BitmapDescriptorFactory.fromBitmap(it)
            } ?: BitmapDescriptorFactory.defaultMarker()
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
}