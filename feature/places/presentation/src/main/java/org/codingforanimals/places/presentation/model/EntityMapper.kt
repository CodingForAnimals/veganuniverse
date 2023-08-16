package org.codingforanimals.places.presentation.model

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import org.codingforanimals.places.presentation.details.entity.PlaceReviewViewEntity
import org.codingforanimals.places.presentation.details.model.Markers
import org.codingforanimals.places.presentation.details.model.OpeningHours
import org.codingforanimals.places.presentation.entity.PlaceViewEntity
import org.codingforanimals.veganuniverse.core.ui.place.DayOfWeek
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.places.entity.Place
import org.codingforanimals.veganuniverse.places.entity.PlaceReview

private const val TAG = "EntityMapper"

internal fun Place.toViewEntity(): PlaceViewEntity? {
    return try {
        val type = PlaceType.valueOf(type)
        PlaceViewEntity(
            id = id,
            name = name,
            addressComponents = addressComponents,
            imageRef = imageRef,
            description = description,
            type = type,
            tags = tags.map { PlaceTag.valueOf(it) },
            rating = rating,
            reviewCount = reviewCount,
            state = MarkerState(LatLng(latitude, longitude)),
            marker = getMarker(type),
            timestamp = timestamp,
            openingHours = openingHours.toViewEntity(),
        )
    } catch (e: Throwable) {
        Log.e(TAG, e.stackTraceToString())
        null
    }
}

private fun getMarker(type: PlaceType) = when (type) {
    PlaceType.RESTAURANT -> Markers.restaurantMarker
    PlaceType.CAFE -> Markers.cafeMarker
    PlaceType.STORE -> Markers.storeMarker
    PlaceType.BAR -> Markers.storeMarker
}

internal fun List<org.codingforanimals.veganuniverse.places.entity.OpeningHours>.toViewEntity(): List<OpeningHours> =
    mapNotNull { it.toViewEntity() }

internal fun org.codingforanimals.veganuniverse.places.entity.OpeningHours.toViewEntity(): OpeningHours? {
    return try {
        OpeningHours(
            dayOfWeek = DayOfWeek.valueOf(dayOfWeek),
            mainPeriod = mainPeriod,
            secondaryPeriod = secondaryPeriod,
        )
    } catch (e: Throwable) {
        null
    }
}

fun PlaceReview.toViewEntity(): PlaceReviewViewEntity {
    return PlaceReviewViewEntity(
        id = id,
        username = username,
        rating = rating,
        title = title,
        description = description,
        timestamp = timestampSeconds.toString()
    )
}