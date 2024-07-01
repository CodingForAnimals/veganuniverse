package org.codingforanimals.veganuniverse.commons.product.presentation

import org.codingforanimals.veganuniverse.commons.product.presentation.R
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductSorter

val ProductSorter.label: Int
    get() = when (this) {
        ProductSorter.NAME -> R.string.name
        ProductSorter.DATE -> R.string.most_recent
    }