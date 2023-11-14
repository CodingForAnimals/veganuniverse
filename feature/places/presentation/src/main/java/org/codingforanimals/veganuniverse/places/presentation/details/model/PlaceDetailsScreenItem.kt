package org.codingforanimals.veganuniverse.places.presentation.details.model

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import org.codingforanimals.veganuniverse.places.entity.AddressComponents
import org.codingforanimals.veganuniverse.places.ui.PlaceMarker
import org.codingforanimals.veganuniverse.places.ui.PlaceTag

internal sealed class PlaceDetailsScreenItem {
    data class Hero(val url: String?) : PlaceDetailsScreenItem()

    data class Header(
        val title: String,
        val rating: Int,
    ) : PlaceDetailsScreenItem()

    data class AddressAndOpeningHours(
        val addressComponents: AddressComponents,
        val openingHours: List<OpeningHours>,
    ) : PlaceDetailsScreenItem()

    data class Description(val description: String) : PlaceDetailsScreenItem()

    data class Tags(val tags: List<PlaceTag>) : PlaceDetailsScreenItem()

    data class StaticMap(
        val marker: PlaceMarker,
        private val latLng: LatLng,
    ) : PlaceDetailsScreenItem() {
        val cameraPositionState =
            CameraPositionState(CameraPosition.fromLatLngZoom(latLng, DEFAULT_ZOOM))

        companion object {
            private const val DEFAULT_ZOOM = 15f
        }
    }

    data object Reviews : PlaceDetailsScreenItem()
}
