package org.codingforanimals.veganuniverse.place.presentation.model

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.place.model.DayOfWeek
import org.codingforanimals.veganuniverse.place.presentation.R

val DayOfWeek.label: Int
    @StringRes get() = when (this) {
        DayOfWeek.SUNDAY -> R.string.day_of_week_sunday
        DayOfWeek.MONDAY -> R.string.day_of_week_monday
        DayOfWeek.TUESDAY -> R.string.day_of_week_tuesday
        DayOfWeek.WEDNESDAY -> R.string.day_of_week_wednesday
        DayOfWeek.THURSDAY -> R.string.day_of_week_thursday
        DayOfWeek.FRIDAY -> R.string.day_of_week_friday
        DayOfWeek.SATURDAY -> R.string.day_of_week_saturday
    }