package org.codingforanimals.veganuniverse.product.domain.model

import android.util.Log

enum class ProductSorter {
    NAME,
    DATE,
    ;

    companion object {
        private const val TAG = "ProductSorter"
        fun fromString(value: String?): ProductSorter? {
            return runCatching {
                value?.let { ProductSorter.valueOf(it) }
            }.onFailure {
                Log.i(TAG, it.message ?: it.stackTraceToString())
            }.getOrNull()
        }
    }
}