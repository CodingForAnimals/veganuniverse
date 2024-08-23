package org.codingforanimals.veganuniverse.place.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.ui.calendar.DayOfWeek

data class OpeningHoursUI(
    val dayOfWeek: DayOfWeek,
    val mainPeriod: Period? = null,
    val secondaryPeriod: Period? = null,
) {

    val dayStringRes = when (dayOfWeek) {
        DayOfWeek.SUNDAY -> R.string.day_of_week_sunday
        DayOfWeek.MONDAY -> R.string.day_of_week_monday
        DayOfWeek.TUESDAY -> R.string.day_of_week_tuesday
        DayOfWeek.WEDNESDAY -> R.string.day_of_week_wednesday
        DayOfWeek.THURSDAY -> R.string.day_of_week_thursday
        DayOfWeek.FRIDAY -> R.string.day_of_week_friday
        DayOfWeek.SATURDAY -> R.string.day_of_week_saturday
    }

    companion object {
        fun defaultPeriod() = Period(9, 0, 20, 0)
        fun fromModel(model: OpeningHours): OpeningHoursUI? {
            return OpeningHoursUI(
                dayOfWeek = DayOfWeek.fromString(model.dayOfWeek) ?: return null,
                mainPeriod = model.mainPeriod ?: defaultPeriod(),
                secondaryPeriod = model.secondaryPeriod,
            )
        }
    }
}

@Composable
fun OpeningHours.toUI(): OpeningHoursUI? {
    return remember {
        OpeningHoursUI(
            dayOfWeek = DayOfWeek.fromString(dayOfWeek) ?: return@remember null,
            mainPeriod = mainPeriod,
            secondaryPeriod = secondaryPeriod,
        )
    }
}