package org.codingforanimals.veganuniverse.commons.navigation.model

data class DeepLinkNavigationOptions(
    val popUpTo: String? = null,
    val inclusive: Boolean = false,
) {
    class Builder {
        private var popUpTo: String? = null
        private var inclusive: Boolean = false

        fun popUpTo(route: String, inclusive: Boolean = false): Builder {
            popUpTo = route
            this.inclusive = inclusive
            return this
        }

        fun build(): DeepLinkNavigationOptions {
            return DeepLinkNavigationOptions(popUpTo, inclusive)
        }
    }
}