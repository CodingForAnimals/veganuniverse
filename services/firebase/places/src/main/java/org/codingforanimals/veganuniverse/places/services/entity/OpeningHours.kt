package org.codingforanimals.veganuniverse.places.services.entity

internal data class OpeningHours(
    val dayOfWeek: String = "",
    val mainPeriod: Period? = null,
    val secondaryPeriod: Period? = null,
)