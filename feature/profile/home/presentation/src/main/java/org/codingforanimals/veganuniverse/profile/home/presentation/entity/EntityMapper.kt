package org.codingforanimals.veganuniverse.profile.home.presentation.entity

import android.util.Log
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.utils.administrativeArea
import org.codingforanimals.veganuniverse.places.ui.PlaceCardItem
import org.codingforanimals.veganuniverse.places.ui.PlaceMarker
import org.codingforanimals.veganuniverse.places.ui.PlaceTag
import org.codingforanimals.veganuniverse.places.ui.PlaceType

private const val TAG = "EntityMapper"

internal fun Place.toCard(): PlaceCardItem? {
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
            tags = tags.map { PlaceTag.valueOf(it) },
            timestamp = timestamp,
            latitude = latitude,
            longitude = longitude,
            marker = PlaceMarker.getMarker(type),
        )
    } catch (e: Throwable) {
        Log.e(TAG, e.stackTraceToString())
        null
    }
}