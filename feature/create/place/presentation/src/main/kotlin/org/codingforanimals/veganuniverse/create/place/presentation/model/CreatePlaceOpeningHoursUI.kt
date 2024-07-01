package org.codingforanimals.veganuniverse.create.place.presentation.model

import org.codingforanimals.veganuniverse.commons.place.domain.model.DayOfWeek
import org.codingforanimals.veganuniverse.commons.place.shared.model.OpeningHours
import org.codingforanimals.veganuniverse.commons.place.shared.model.Period

data class CreatePlaceOpeningHoursUI(
    val dayOfWeek: DayOfWeek,
    val isClosed: Boolean = false,
    val isSplit: Boolean = false,
    val mainPeriod: Period = defaultPeriod(),
    val secondaryPeriod: Period = defaultPeriod(),
) {

    companion object {
        fun defaultPeriod() = Period(9, 0, 20, 0)
        fun fromModel(model: OpeningHours): CreatePlaceOpeningHoursUI? {
            return CreatePlaceOpeningHoursUI(
                dayOfWeek = DayOfWeek.fromString(model.dayOfWeek) ?: return null,
                mainPeriod = model.mainPeriod ?: defaultPeriod(),
                secondaryPeriod = model.secondaryPeriod ?: defaultPeriod(),
            )
        }
    }
}