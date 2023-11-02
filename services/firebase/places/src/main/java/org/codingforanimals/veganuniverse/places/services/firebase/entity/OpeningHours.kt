package org.codingforanimals.veganuniverse.places.services.firebase.entity

internal data class OpeningHours(
    val dayOfWeek: String = "",
    val mainPeriod: Period? = null,
    val secondaryPeriod: Period? = null,
)