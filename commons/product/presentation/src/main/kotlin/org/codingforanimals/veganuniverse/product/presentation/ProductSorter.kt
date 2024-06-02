package org.codingforanimals.veganuniverse.product.presentation

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.product.model.ProductSorter

val ProductSorter.label: Int
    get() = when (this) {
        ProductSorter.NAME -> R.string.name
        ProductSorter.DATE -> R.string.most_recent
    }