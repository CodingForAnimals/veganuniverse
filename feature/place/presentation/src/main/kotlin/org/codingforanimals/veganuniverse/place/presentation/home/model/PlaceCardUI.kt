package org.codingforanimals.veganuniverse.place.presentation.home.model

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceCard
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceTag
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceType

data class PlaceCardUI(
    val geoHash: String,
    val name: String,
    val rating: Double?,
    val streetAddress: String,
    val administrativeArea: String,
    val type: PlaceType?,
    val tags: List<PlaceTag>,
    val imageUrl: String?,
    val markerState: MarkerState,
) {
    val fullStreetAddress: String = "$streetAddress, $administrativeArea"
}

internal fun PlaceCard.toUI(): PlaceCardUI {
    return PlaceCardUI(
        geoHash = geoHash,
        name = name.orEmpty(),
        rating = rating,
        streetAddress = streetAddress.orEmpty(),
        administrativeArea = administrativeArea.orEmpty(),
        type = type,
        tags = tags.orEmpty(),
        imageUrl = imageUrl,
        markerState = runCatching { MarkerState(LatLng(latitude, longitude)) }.getOrElse {
            Log.e("pepe", "$name ${it.stackTraceToString()}")
            MarkerState()
        }
    )
}
