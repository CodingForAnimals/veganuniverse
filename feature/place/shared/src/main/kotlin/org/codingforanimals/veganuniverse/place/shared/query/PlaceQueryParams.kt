package org.codingforanimals.veganuniverse.place.shared.query

class PlaceQueryParams private constructor(
    val validated: Boolean,
    val pageSize: Int,
    val maxSize: Int,
) {
    class Builder {
        private var validated: Boolean = true
        private var pageSize: Int = 10
        private var maxSize: Int = Int.MAX_VALUE

        fun withValidated(value: Boolean): Builder {
            validated = value
            return this
        }

        fun withPageSize(value: Int): Builder {
            pageSize = value
            return this
        }

        fun withMaxSize(value: Int): Builder {
            maxSize = value
            return this
        }

        fun build(): PlaceQueryParams {
            return PlaceQueryParams(validated, pageSize, maxSize)
        }
    }
}
