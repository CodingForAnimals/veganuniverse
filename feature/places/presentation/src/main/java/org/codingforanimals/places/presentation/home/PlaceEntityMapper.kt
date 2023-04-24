package org.codingforanimals.places.presentation.home

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import org.codingforanimals.places.presentation.home.composables.Markers
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.places.domain.PlaceDomainEntity

internal fun List<PlaceDomainEntity>.toViewEntity() = mapNotNull { it.toViewEntity() }

internal fun PlaceDomainEntity.toViewEntity(): PlaceViewEntity? {
    val location = getLocationFromId(id) ?: return null
    val type = getType(type) ?: return null
    return PlaceViewEntity(
        id = id,
        imageRef = imageRef,
        type = type,
        tags = getTags(),
        name = name,
        rating = rating,
        address = address,
        city = city,
        state = MarkerState(location),
        marker = getMarker(type)
    )
}

private fun getMarker(type: PlaceType) = when (type) {
    PlaceType.RESTAURANT -> Markers.restaurantMarker
    PlaceType.CAFE -> Markers.cafeMarker
    PlaceType.STORE -> Markers.storeMarker
}

internal fun PlaceDomainEntity.getTags(): List<PlaceTag> {
    return tags.mapNotNull {
        try {
            PlaceTag.valueOf(it)
        } catch (e: Throwable) {
            Log.e("PlaceEntityMapper.kt", "Can't parse tag ${e.stackTraceToString()}")
            null
        }
    }
}

private fun getType(type: String): PlaceType? {
    return try {
        PlaceType.valueOf(type)
    } catch (e: Exception) {
        Log.e("PlaceEntityMapper.kt", "Error mapping PlaceType")
        null
    }
}

private fun getLocationFromId(id: String): LatLng? {
    val location = id.split(":")
    val latLng = try {
        LatLng(location[0].toDouble(), location[1].toDouble())
    } catch (e: Throwable) {
        Log.e(
            "PlaceEntityMapper.kt",
            "Unable to get location from id. Msg: ${e.stackTraceToString()}"
        )
        null
    }
    return latLng
}