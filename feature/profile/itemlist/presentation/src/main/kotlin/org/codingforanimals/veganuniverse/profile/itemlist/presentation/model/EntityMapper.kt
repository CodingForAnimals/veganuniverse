package org.codingforanimals.veganuniverse.profile.itemlist.presentation.model

import android.util.Log
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.utils.administrativeArea
import org.codingforanimals.veganuniverse.places.ui.PlaceCardItem
import org.codingforanimals.veganuniverse.places.ui.PlaceMarker
import org.codingforanimals.veganuniverse.places.ui.PlaceTag
import org.codingforanimals.veganuniverse.places.ui.PlaceType

private const val TAG = "EntityMapper"

internal fun Place.toPlaceCardItem(): PlaceCardItem? {
    return try {
        val type = PlaceType.valueOf(type)
        PlaceCardItem(
            geoHash = geoHash,
            name = name,
            rating = rating,
            streetAddress = addressComponents.streetAddress,
            administrativeArea = addressComponents.administrativeArea,
            type = type,
            imageRef = imageRef,
            tags = getTags(),
            timestamp = timestamp,
            marker = getMarker(type),
            latitude = latitude,
            longitude = longitude,
        )
    } catch (e: Throwable) {
        Log.e(TAG, e.stackTraceToString())
        null
    }
}

private fun Place.getTags(): List<PlaceTag> {
    return tags.mapNotNull {
        try {
            PlaceTag.valueOf(it)
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
            null
        }
    }
}

private fun getMarker(type: PlaceType) = when (type) {
    PlaceType.RESTAURANT -> PlaceMarker.RestaurantMarker
    PlaceType.CAFE -> PlaceMarker.CafeMarker
    PlaceType.STORE -> PlaceMarker.StoreMarker
    PlaceType.BAR -> PlaceMarker.StoreMarker
}