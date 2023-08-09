package org.codingforanimals.places.presentation.model

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import org.codingforanimals.places.presentation.details.model.DayOfWeek
import org.codingforanimals.places.presentation.details.model.Markers
import org.codingforanimals.places.presentation.details.model.OpeningHours
import org.codingforanimals.veganuniverse.core.ui.place.PlaceTag
import org.codingforanimals.veganuniverse.core.ui.place.PlaceType
import org.codingforanimals.veganuniverse.places.domain.model.PlaceDomainEntity
import org.codingforanimals.veganuniverse.places.domain.model.ReviewDomainEntity

internal fun PlaceDomainEntity.toViewEntity(): PlaceViewEntity? {
    val type = getType(type) ?: return null
    return PlaceViewEntity(
        id = id,
        imageRef = imageRef,
        description = description,
        type = type,
        tags = getTags(),
        name = name,
        rating = rating,
        reviewCount = reviewCount,
        address = address,
        city = city,
        state = MarkerState(LatLng(latitude, longitude)),
        marker = getMarker(type),
        timestamp = timestamp,
        openingHours = openingHours.toViewEntity(),
    )
}

private fun getMarker(type: PlaceType) = when (type) {
    PlaceType.RESTAURANT -> Markers.restaurantMarker
    PlaceType.CAFE -> Markers.cafeMarker
    PlaceType.STORE -> Markers.storeMarker
    PlaceType.BAR -> Markers.storeMarker
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

internal fun List<org.codingforanimals.veganuniverse.shared.entity.places.OpeningHours>.toViewEntity(): List<OpeningHours> =
    mapNotNull { it.toViewEntity() }

internal fun org.codingforanimals.veganuniverse.shared.entity.places.OpeningHours.toViewEntity(): OpeningHours? {
    return try {
        OpeningHours(
            dayOfWeek = DayOfWeek.valueOf(dayOfWeek),
            mainPeriod = mainPeriod?.toViewEntity(),
            secondaryPeriod = secondaryPeriod?.toViewEntity(),
        )
    } catch (e: Throwable) {
        null
    }
}

private fun org.codingforanimals.veganuniverse.shared.entity.places.OpeningHours.Period.toViewEntity() =
    OpeningHours.Period(
        openingHour = openingHour,
        openingMinute = openingMinute,
        closingHour = closingHour,
        closingMinute = closingMinute
    )

fun ReviewDomainEntity.toViewEntity(): ReviewViewEntity {
    return ReviewViewEntity(
        username = username,
        rating = rating,
        title = title,
        description = description,
        timestamp = timestamp.toString()
    )
}