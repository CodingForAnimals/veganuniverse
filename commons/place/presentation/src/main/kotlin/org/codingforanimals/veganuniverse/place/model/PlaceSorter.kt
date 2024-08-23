package org.codingforanimals.veganuniverse.place.model

import org.codingforanimals.veganuniverse.place.presentation.R

val PlaceSorter.label: Int
    get() = when (this) {
        PlaceSorter.NAME -> R.string.place_sorter_label_name
        PlaceSorter.DATE -> R.string.place_sorter_label_date
        PlaceSorter.RATING -> R.string.place_sorter_label_rating
        PlaceSorter.REVIEWS -> R.string.place_sorter_label_reviews
    }