package org.codingforanimals.veganuniverse.create.presentation.place.model

import com.google.android.libraries.places.api.model.DayOfWeek
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_friday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_monday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_saturday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_sunday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_thursday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_tuesday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_wednesday
import org.codingforanimals.veganuniverse.core.ui.utils.formatTimeToString

data class OpeningHours(
    val dayOfWeek: DayOfWeek,
    val isClosed: Boolean = false,
    val isSplit: Boolean = false,
    val mainPeriod: Period = Period.defaultPeriod(),
    val secondaryPeriod: Period = Period.defaultPeriod(),
) {

    val dayStringRes = when (dayOfWeek) {
        DayOfWeek.SUNDAY -> day_of_week_sunday
        DayOfWeek.MONDAY -> day_of_week_monday
        DayOfWeek.TUESDAY -> day_of_week_tuesday
        DayOfWeek.WEDNESDAY -> day_of_week_wednesday
        DayOfWeek.THURSDAY -> day_of_week_thursday
        DayOfWeek.FRIDAY -> day_of_week_friday
        DayOfWeek.SATURDAY -> day_of_week_saturday
    }

    data class Period(
        val openingHour: Int,
        val openingMinute: Int,
        val closingHour: Int,
        val closingMinute: Int,
    ) {
        companion object {
            fun defaultPeriod() = Period(9, 0, 20, 0)
        }

        val fromDisplayPeriod: String =
            "${openingHour.formatTimeToString()}:${openingMinute.formatTimeToString()}"

        val toDisplayPeriod: String =
            "${closingHour.formatTimeToString()}:${closingMinute.formatTimeToString()}"

        val displayPeriod: String = "$fromDisplayPeriod-$toDisplayPeriod"
    }
}