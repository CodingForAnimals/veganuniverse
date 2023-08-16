package org.codingforanimals.places.presentation.details.model

import org.codingforanimals.veganuniverse.core.ui.place.DayOfWeek
import org.codingforanimals.veganuniverse.places.entity.Period

internal data class OpeningHours(
    val dayOfWeek: DayOfWeek,
    val mainPeriod: Period?,
    val secondaryPeriod: Period?,
)