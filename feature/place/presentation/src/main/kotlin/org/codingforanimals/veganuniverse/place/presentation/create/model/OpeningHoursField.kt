package org.codingforanimals.veganuniverse.place.presentation.create.model

import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.place.model.DayOfWeek

data class OpeningHoursField(
    private val openingHours: List<CreatePlaceOpeningHoursUI> = defaultOpeningHours,
    val isEditing: Boolean = false,
    val isExpanded: Boolean = true,
) {
    val sortedOpeningHours = openingHours.sortedBy { it.dayOfWeek.ordinal }
    val expandIcon = if (isExpanded) VUIcons.ArrowDropUp else VUIcons.ArrowDropDown

    companion object {
        private val defaultOpeningHours = DayOfWeek.entries.map { CreatePlaceOpeningHoursUI(it) }
    }
}
