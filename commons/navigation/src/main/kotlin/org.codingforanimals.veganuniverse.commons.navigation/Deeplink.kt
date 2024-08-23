package org.codingforanimals.veganuniverse.commons.navigation

sealed class Deeplink(val value: String) {
    data object Reauthentication : Deeplink("reauthentication")

    val deeplink: String
        get() = getDeeplink(value)

    private fun getDeeplink(value: String): String {
        return "$SCHEMA$value"
    }

    companion object {
        private const val SCHEMA = "veganuniverse://"
    }
}
