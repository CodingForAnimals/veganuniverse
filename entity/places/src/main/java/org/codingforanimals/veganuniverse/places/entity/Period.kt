package org.codingforanimals.veganuniverse.places.entity

data class Period(
    val openingHour: Int = 0,
    val openingMinute: Int = 0,
    val closingHour: Int = 0,
    val closingMinute: Int = 0,
) {

    val fromDisplayPeriod: String
        get() = "${openingHour.formatTimeToString()}:${openingMinute.formatTimeToString()}"

    val toDisplayPeriod: String
        get() = "${closingHour.formatTimeToString()}:${closingMinute.formatTimeToString()}"

    val displayPeriod: String
        get() = "$fromDisplayPeriod-$toDisplayPeriod"

    private fun Int.formatTimeToString(): String {
        val number = toString()
        return if (number.length == 1) {
            "0$number"
        } else number
    }
}