package org.codingforanimals.veganuniverse.create.presentation.place.entity

import com.google.android.libraries.places.api.model.DayOfWeek
import org.codingforanimals.veganuniverse.core.ui.utils.formatTimeToString

sealed class PlaceDayPeriod(
    val day: DayOfWeek,
) {
    data class Open(
        val periodDay: DayOfWeek,
        val openingHour: Int,
        val openingMinute: Int,
        val closingHour: Int,
        val closingMinute: Int,
    ) : PlaceDayPeriod(periodDay)

    data class Closed(val periodDay: DayOfWeek) : PlaceDayPeriod(periodDay)
}

data class PlaceOpeningPeriod(
    val day: DayOfWeek,
    val openingHour: Int,
    val openingMinute: Int,
    val closingHour: Int,
    val closingMinute: Int,
) {

    private val openDisplayPeriod: String =
        "${openingHour.formatTimeToString()}:${openingMinute.formatTimeToString()}"

    private val closeDisplayPeriod: String =
        "${closingHour.formatTimeToString()}:${closingMinute.formatTimeToString()}"

    val displayPeriod: String = "$openDisplayPeriod-$closeDisplayPeriod"
}