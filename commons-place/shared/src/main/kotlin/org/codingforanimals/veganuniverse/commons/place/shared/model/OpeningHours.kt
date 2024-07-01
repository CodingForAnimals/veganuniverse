package org.codingforanimals.veganuniverse.commons.place.shared.model

data class OpeningHours(
    val dayOfWeek: String = "",
    val mainPeriod: Period? = null,
    val secondaryPeriod: Period? = null,
)