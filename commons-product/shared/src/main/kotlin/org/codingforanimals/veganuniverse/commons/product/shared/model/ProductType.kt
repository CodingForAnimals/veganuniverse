package org.codingforanimals.veganuniverse.commons.product.shared.model

import android.util.Log

enum class ProductType {
    VEGAN,
    NOT_VEGAN,
    DOUBTFUL,
    ;

    companion object {
        private const val TAG = "ProductType"
        fun fromString(value: String?): ProductType? {
            return runCatching {
                value?.let { ProductType.valueOf(it) }
            }.onFailure {
                Log.e(TAG, it.stackTraceToString())
            }.getOrNull()
        }
    }
}