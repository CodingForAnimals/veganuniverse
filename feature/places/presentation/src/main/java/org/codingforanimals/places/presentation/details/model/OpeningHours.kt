package org.codingforanimals.places.presentation.details.model

import org.codingforanimals.veganuniverse.core.ui.utils.formatTimeToString

internal data class OpeningHours(
    val dayOfWeek: DayOfWeek,
    val mainPeriod: Period?,
    val secondaryPeriod: Period?,
) {
    data class Period(
        val openingHour: Int,
        val openingMinute: Int,
        val closingHour: Int,
        val closingMinute: Int,
    ) {
        val fromDisplayPeriod: String =
            "${openingHour.formatTimeToString()}:${openingMinute.formatTimeToString()}"

        val toDisplayPeriod: String =
            "${closingHour.formatTimeToString()}:${closingMinute.formatTimeToString()}"

        val displayPeriod: String = "$fromDisplayPeriod-$toDisplayPeriod"
    }
}