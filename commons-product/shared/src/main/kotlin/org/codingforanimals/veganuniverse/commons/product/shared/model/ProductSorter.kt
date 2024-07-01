package org.codingforanimals.veganuniverse.commons.product.shared.model

enum class ProductSorter {
    NAME,
    DATE,
    ;

    companion object {
        fun fromString(value: String?): ProductSorter? {
            return runCatching {
                value?.let { ProductSorter.valueOf(it) }
            }.getOrNull()
        }
    }
}