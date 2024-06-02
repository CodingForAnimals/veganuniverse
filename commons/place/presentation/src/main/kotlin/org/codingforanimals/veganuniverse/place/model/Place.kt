package org.codingforanimals.veganuniverse.place.model

val Period.fromDisplayPeriod: String
    get() = "${openingHour.formatTimeToString()}:${openingMinute.formatTimeToString()}hs"

val Period.toDisplayPeriod: String
    get() = "${closingHour.formatTimeToString()}:${closingMinute.formatTimeToString()}hs"

val Period.displayPeriod: String
    get() = "$fromDisplayPeriod-$toDisplayPeriod"

val AddressComponents.fullStreetAddress: String
    get() = "$streetAddress ${administrativeArea?.let { "- $it" }}"

val AddressComponents.administrativeArea: String?
    get() = locality?.ifEmpty { secondaryAdminArea?.ifEmpty { primaryAdminArea?.ifEmpty { country } } }

private fun Int.formatTimeToString(): String {
    val number = toString()
    return if (number.length == 1) {
        "0$number"
    } else number
}