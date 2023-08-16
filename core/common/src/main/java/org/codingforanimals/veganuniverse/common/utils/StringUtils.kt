package org.codingforanimals.veganuniverse.common.utils

fun areNotBlank(vararg strings: String?): Boolean {
    for (string in strings) {
        if (string?.isBlank() == true) {
            return false
        }
    }
    return true
}

val emptyString = ""