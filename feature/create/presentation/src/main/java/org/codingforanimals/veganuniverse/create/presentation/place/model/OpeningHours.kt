package org.codingforanimals.veganuniverse.create.presentation.place.model

import com.google.android.libraries.places.api.model.DayOfWeek
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_friday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_monday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_saturday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_sunday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_thursday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_tuesday
import org.codingforanimals.veganuniverse.core.ui.R.string.day_of_week_wednesday
import org.codingforanimals.veganuniverse.places.entity.Period

data class OpeningHours(
    val dayOfWeek: DayOfWeek,
    val isClosed: Boolean = false,
    val isSplit: Boolean = false,
    val mainPeriod: Period = defaultPeriod(),
    val secondaryPeriod: Period = defaultPeriod(),
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

    companion object {
        fun defaultPeriod() = Period(9, 0, 20, 0)
    }
}