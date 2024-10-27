package org.codingforanimals.veganuniverse.place.presentation.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.place.shared.model.AddressComponents
import org.codingforanimals.veganuniverse.place.shared.model.Period
import org.codingforanimals.veganuniverse.place.shared.model.PlaceType

val Period.fromDisplayPeriod: String
    get() = "${openingHour.formatTimeToString()}:${openingMinute.formatTimeToString()}hs"

val Period.toDisplayPeriod: String
    get() = "${closingHour.formatTimeToString()}:${closingMinute.formatTimeToString()}hs"

val Period.displayPeriod: String
    get() = "$fromDisplayPeriod-$toDisplayPeriod"

val AddressComponents.fullStreetAddress: String
    get() = "$streetAddress ${administrativeArea?.let { "- $it" }}"

val AddressComponents.administrativeArea: String?
    get() = locality?.ifEmpty { secondaryAdminArea?.ifEmpty { primaryAdminArea?.ifEmpty { country } } }

private fun Int.formatTimeToString(): String {
    val number = toString()
    return if (number.length == 1) {
        "0$number"
    } else number
}

sealed class PlaceMarker(
    private val defaultIcon: Icon.DrawableResourceIcon,
    private val selectedIcon: Icon.DrawableResourceIcon,
) {
    @Composable
    fun getDisplayMarker(isSelected: Boolean): Bitmap? {
        val context = LocalContext.current
        return remember(isSelected) {
            val resId = if (isSelected) selectedIcon.id else defaultIcon.id
            createBitmap(context, resId)
        }
    }

    fun getDisplayMarker(isSelected: Boolean, context: Context): Bitmap? {
        val resId = if (isSelected) selectedIcon.id else defaultIcon.id
        return createBitmap(context, resId)
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

    data object RestaurantMarker : PlaceMarker(
        defaultIcon = VUIcons.MarkerRestaurant,
        selectedIcon = VUIcons.MarkerRestaurantSelected,
    )

    data object StoreMarker : PlaceMarker(
        defaultIcon = VUIcons.MarkerStore,
        selectedIcon = VUIcons.MarkerStoreSelected,
    )

    data object CafeMarker : PlaceMarker(
        defaultIcon = VUIcons.MarkerCafe,
        selectedIcon = VUIcons.MarkerCafeSelected,
    )

    data object BarMarker : PlaceMarker(
        defaultIcon = VUIcons.Beer,
        selectedIcon = VUIcons.Beer,
    )

    data object DefaultMarker : PlaceMarker(
        defaultIcon = VUIcons.LocationFilled,
        selectedIcon = VUIcons.LocationFilled,
    )

    companion object {
        fun getMarker(type: PlaceType?) = when (type) {
            PlaceType.RESTAURANT -> RestaurantMarker
            PlaceType.CAFE -> CafeMarker
            PlaceType.STORE -> StoreMarker
            PlaceType.BAR -> BarMarker
            null -> DefaultMarker
        }
    }
}