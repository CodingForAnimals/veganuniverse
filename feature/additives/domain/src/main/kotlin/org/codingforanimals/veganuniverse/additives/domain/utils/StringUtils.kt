package org.codingforanimals.veganuniverse.additives.domain.utils

internal fun String.accentInsensitive(): String {
    return lowercase()
        .replace("á", "a")
        .replace("é", "e")
        .replace("í", "i")
        .replace("ó", "o")
        .replace("ú", "u")
}
