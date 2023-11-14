package org.codingforanimals.veganuniverse.places.presentation.model

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import kotlin.math.roundToInt
import org.codingforanimals.veganuniverse.places.presentation.details.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.presentation.details.model.OpeningHours
import org.codingforanimals.veganuniverse.places.presentation.entity.Place
import org.codingforanimals.veganuniverse.places.ui.PlaceCardItem
import org.codingforanimals.veganuniverse.places.ui.PlaceMarker
import org.codingforanimals.veganuniverse.places.ui.PlaceTag
import org.codingforanimals.veganuniverse.places.ui.PlaceType
import org.codingforanimals.veganuniverse.ui.calendar.DayOfWeek
import org.codingforanimals.veganuniverse.places.entity.OpeningHours as OpeningHoursDomainEntity
import org.codingforanimals.veganuniverse.places.entity.Place as PlaceDomainEntity
import org.codingforanimals.veganuniverse.places.entity.PlaceCard as PlaceCardDomainEntity
import org.codingforanimals.veganuniverse.places.entity.PlaceReview as PlaceReviewDomainEntity

private const val TAG = "EntityMapper"

internal fun PlaceCardDomainEntity.toViewEntity(): PlaceCardItem? {
    return try {
        val type = PlaceType.valueOf(type)
        PlaceCardItem(
            geoHash = geoHash,
            name = name,
            rating = rating.roundToInt(),
            streetAddress = streetAddress,
            administrativeArea = administrativeArea,
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

private fun PlaceCardDomainEntity.getTags(): List<PlaceTag> {
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

internal fun PlaceDomainEntity.toViewEntity(): Place? {
    return try {
        val type = PlaceType.valueOf(type)
        Place(
            geoHash = geoHash,
            name = name,
            addressComponents = addressComponents,
            imageRef = imageRef,
            description = description,
            type = type,
            tags = tags.map { PlaceTag.valueOf(it) },
            rating = rating,
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

internal fun List<OpeningHoursDomainEntity>.toViewEntity(): List<OpeningHours> =
    mapNotNull { it.toViewEntity() }

internal fun OpeningHoursDomainEntity.toViewEntity(): OpeningHours? {
    return try {
        OpeningHours(
            dayOfWeek = DayOfWeek.valueOf(dayOfWeek),
            mainPeriod = mainPeriod,
            secondaryPeriod = secondaryPeriod,
        )
    } catch (e: Throwable) {
        Log.e(TAG, e.stackTraceToString())
        null
    }
}

fun PlaceReviewDomainEntity.toViewEntity(): PlaceReview {
    return PlaceReview(
        id = id,
        userId = userId,
        username = username,
        rating = rating,
        title = title,
        description = description,
        timestamp = timestampSeconds.toString()
    )
}