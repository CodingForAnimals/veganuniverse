package org.codingforanimals.veganuniverse.commons.place.presentation.model

import org.codingforanimals.veganuniverse.commons.place.presentation.R
import org.codingforanimals.veganuniverse.commons.place.shared.model.PlaceCardSorter

val PlaceCardSorter.label: Int
    get() = when (this) {
        PlaceCardSorter.NAME -> R.string.place_sorter_label_name
        PlaceCardSorter.RATING -> R.string.place_sorter_label_rating
    }