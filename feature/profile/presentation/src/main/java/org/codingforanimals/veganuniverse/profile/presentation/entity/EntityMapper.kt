package org.codingforanimals.veganuniverse.profile.presentation.entity

import android.util.Log
import org.codingforanimals.veganuniverse.core.ui.place.PlaceMarker
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.utils.administrativeArea
import org.codingforanimals.veganuniverse.places.ui.entity.PlaceCard

private const val TAG = "EntityMapper"

internal fun Place.toCard(): PlaceCard? {
    return try {
        val type = PlaceType.valueOf(type)
        PlaceCard(
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