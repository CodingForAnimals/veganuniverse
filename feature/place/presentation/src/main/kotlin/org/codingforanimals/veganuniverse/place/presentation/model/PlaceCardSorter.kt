package org.codingforanimals.veganuniverse.place.presentation.model

import org.codingforanimals.veganuniverse.place.presentation.R
import org.codingforanimals.veganuniverse.place.shared.model.PlaceCardSorter

val PlaceCardSorter.label: Int
    get() = when (this) {
        PlaceCardSorter.NAME -> R.string.place_sorter_label_name
        PlaceCardSorter.RATING -> R.string.place_sorter_label_rating
    }