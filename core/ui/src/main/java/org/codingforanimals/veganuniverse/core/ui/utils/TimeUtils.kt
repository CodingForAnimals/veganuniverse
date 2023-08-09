package org.codingforanimals.veganuniverse.core.ui.utils

fun Int.formatTimeToString(): String {
    val number = toString()
    return if (number.length == 1) {
        "0$number"
    } else number
}