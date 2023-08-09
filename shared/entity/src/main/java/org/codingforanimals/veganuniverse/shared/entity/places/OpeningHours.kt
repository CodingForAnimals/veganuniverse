package org.codingforanimals.veganuniverse.shared.entity.places

data class OpeningHours(
    val dayOfWeek: String = "",
    val mainPeriod: Period? = null,
    val secondaryPeriod: Period? = null,
) {
    data class Period(
        val openingHour: Int = 0,
        val openingMinute: Int = 0,
        val closingHour: Int = 0,
        val closingMinute: Int = 0,
    )
}