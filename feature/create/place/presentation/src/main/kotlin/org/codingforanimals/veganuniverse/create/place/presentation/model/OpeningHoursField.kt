package org.codingforanimals.veganuniverse.create.place.presentation.model

import org.codingforanimals.veganuniverse.ui.calendar.DayOfWeek
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

data class OpeningHoursField(
    private val openingHours: List<CreatePlaceOpeningHoursUI> = defaultOpeningHours,
    val isEditing: Boolean = false,
    val isExpanded: Boolean = true,
) {
    val sortedOpeningHours = openingHours.sortedBy { it.dayOfWeek.ordinal }
    val expandIcon = if (isExpanded) VUIcons.ArrowDropUp else VUIcons.ArrowDropDown

    companion object {
        private val defaultOpeningHours = listOf(
            CreatePlaceOpeningHoursUI(DayOfWeek.SUNDAY),
            CreatePlaceOpeningHoursUI(DayOfWeek.MONDAY),
            CreatePlaceOpeningHoursUI(DayOfWeek.TUESDAY),
            CreatePlaceOpeningHoursUI(DayOfWeek.WEDNESDAY),
            CreatePlaceOpeningHoursUI(DayOfWeek.THURSDAY),
            CreatePlaceOpeningHoursUI(DayOfWeek.FRIDAY),
            CreatePlaceOpeningHoursUI(DayOfWeek.SATURDAY),
        )
    }
}
