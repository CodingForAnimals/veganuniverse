package org.codingforanimals.veganuniverse.create.place.presentation.model

import org.codingforanimals.veganuniverse.commons.place.domain.model.DayOfWeek
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

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
