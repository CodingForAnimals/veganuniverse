package org.codingforanimals.veganuniverse.create.presentation.place.model

import org.codingforanimals.veganuniverse.core.ui.icons.VUIcons
import org.codingforanimals.veganuniverse.core.ui.place.DayOfWeek

data class OpeningHoursField(
    private val openingHours: List<OpeningHours> = defaultOpeningHours,
    val isEditing: Boolean = false,
    val isExpanded: Boolean = true,
) {
    val sortedOpeningHours = openingHours.sortedBy { it.dayOfWeek.ordinal }
    val expandIcon = if (isExpanded) VUIcons.ArrowDropUp else VUIcons.ArrowDropDown

    companion object {
        private val defaultOpeningHours = listOf(
            OpeningHours(DayOfWeek.SUNDAY),
            OpeningHours(DayOfWeek.MONDAY),
            OpeningHours(DayOfWeek.TUESDAY),
            OpeningHours(DayOfWeek.WEDNESDAY),
            OpeningHours(DayOfWeek.THURSDAY),
            OpeningHours(DayOfWeek.FRIDAY),
            OpeningHours(DayOfWeek.SATURDAY),
        )
    }
}
