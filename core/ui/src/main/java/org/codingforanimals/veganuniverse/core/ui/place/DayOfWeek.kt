package org.codingforanimals.veganuniverse.core.ui.place

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.core.ui.R

enum class DayOfWeek(@StringRes val day: Int) {
    SUNDAY(R.string.day_of_week_sunday),
    MONDAY(R.string.day_of_week_monday),
    TUESDAY(R.string.day_of_week_tuesday),
    WEDNESDAY(R.string.day_of_week_wednesday),
    THURSDAY(R.string.day_of_week_thursday),
    FRIDAY(R.string.day_of_week_friday),
    SATURDAY(R.string.day_of_week_saturday);
}