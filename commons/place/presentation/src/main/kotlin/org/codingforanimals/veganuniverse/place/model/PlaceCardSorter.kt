package org.codingforanimals.veganuniverse.place.model

import org.codingforanimals.veganuniverse.place.presentation.R

val PlaceCardSorter.label: Int
    get() = when (this) {
        PlaceCardSorter.NAME -> R.string.place_sorter_label_name
        PlaceCardSorter.RATING -> R.string.place_sorter_label_rating
    }