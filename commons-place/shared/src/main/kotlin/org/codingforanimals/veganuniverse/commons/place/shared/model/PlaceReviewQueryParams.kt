package org.codingforanimals.veganuniverse.commons.place.shared.model

data class PlaceReviewQueryParams internal constructor(
    val placeId: String,
    val sorter: PlaceReviewSorter,
    val userFilter: PlaceReviewUserFilter?,
    val pageSize: Int,
    val maxSize: Int,
) {
    class Builder(
        private val placeId: String,
    ) {
        private var sorter: PlaceReviewSorter = PlaceReviewSorter.DATE
        private var userFilter: PlaceReviewUserFilter? = null
        private var pageSize: Int = 1
        private var maxSize: Int = Int.MAX_VALUE

        fun withSorter(value: PlaceReviewSorter): Builder {
            sorter = value
            return this
        }

        fun withUserFilter(value: PlaceReviewUserFilter): Builder {
            userFilter = value
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

        fun build(): PlaceReviewQueryParams {
            return PlaceReviewQueryParams(
                placeId = placeId,
                sorter = sorter,
                userFilter = userFilter,
                pageSize = pageSize,
                maxSize = maxSize,
            )
        }
    }
}