package org.codingforanimals.veganuniverse.places.presentation.details.model

import org.codingforanimals.veganuniverse.places.entity.Period
import org.codingforanimals.veganuniverse.ui.calendar.DayOfWeek

internal data class OpeningHours(
    val dayOfWeek: DayOfWeek,
    val mainPeriod: Period?,
    val secondaryPeriod: Period?,
)