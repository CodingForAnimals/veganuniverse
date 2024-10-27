package org.codingforanimals.veganuniverse.product.presentation.model

import org.codingforanimals.veganuniverse.product.domain.model.ProductSorter
import org.codingforanimals.veganuniverse.product.presentation.R

val ProductSorter.label: Int
    get() = when (this) {
        ProductSorter.NAME -> R.string.name
        ProductSorter.DATE -> R.string.most_recent
    }