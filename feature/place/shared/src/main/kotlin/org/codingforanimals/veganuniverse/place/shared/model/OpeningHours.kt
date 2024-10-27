package org.codingforanimals.veganuniverse.place.shared.model

import org.codingforanimals.veganuniverse.place.shared.model.Period

data class OpeningHours(
    val dayOfWeek: String = "",
    val mainPeriod: Period? = null,
    val secondaryPeriod: Period? = null,
)