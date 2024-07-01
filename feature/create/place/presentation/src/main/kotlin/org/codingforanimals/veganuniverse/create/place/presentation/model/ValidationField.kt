package org.codingforanimals.veganuniverse.create.place.presentation.model

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import org.codingforanimals.veganuniverse.commons.place.presentation.model.PlaceMarker
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceTag
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceType
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.ValidationField

data class LocationField(
    val latLng: LatLng? = null,
) : ValidationField() {
    override val isValid: Boolean = latLng != null
}

data class AddressField(
    val streetAddress: String,
    val locality: String,
    val primaryAdminArea: String,
    val secondaryAdminArea: String,
    val country: String,
) : ValidationField() {
    override val isValid: Boolean = areNotBlank(streetAddress, locality, country)
}

data class TypeField(
    val value: PlaceType? = null,
) : ValidationField() {
    override val isValid: Boolean = value != null

    @Composable
    fun getIcon(): BitmapDescriptor {
        val marker = when (value) {
            PlaceType.STORE -> PlaceMarker.StoreMarker
            PlaceType.RESTAURANT -> PlaceMarker.RestaurantMarker
            PlaceType.CAFE -> PlaceMarker.CafeMarker
            PlaceType.BAR -> PlaceMarker.BarMarker
            null -> PlaceMarker.DefaultMarker
        }
        return marker.getDisplayMarker(false)
            ?.let { BitmapDescriptorFactory.fromBitmap(it) }
            ?: BitmapDescriptorFactory.defaultMarker()
    }

    val marker: PlaceMarker
        get() = when (value) {
            PlaceType.STORE -> PlaceMarker.StoreMarker
            PlaceType.RESTAURANT -> PlaceMarker.RestaurantMarker
            PlaceType.CAFE -> PlaceMarker.CafeMarker
            PlaceType.BAR -> PlaceMarker.BarMarker
            null -> PlaceMarker.DefaultMarker
        }
}

data class SelectedTagsField(
    val tags: List<PlaceTag> = emptyList(),
) : ValidationField() {
    override val isValid: Boolean = true

    fun getUpdatedSelectedTags(tag: PlaceTag): SelectedTagsField {
        val list = tags.toMutableList()
        if (!list.remove(tag)) {
            list.add(tag)
        }
        return SelectedTagsField(list)
    }

    fun contains(tag: PlaceTag): Boolean {
        return tags.contains(tag)
    }
}

private fun areNotBlank(vararg strings: String?): Boolean {
    for (string in strings) {
        if (string?.isBlank() == true) {
            return false
        }
    }
    return true
}