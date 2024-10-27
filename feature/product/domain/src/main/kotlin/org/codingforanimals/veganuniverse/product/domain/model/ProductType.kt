package org.codingforanimals.veganuniverse.product.domain.model

import android.util.Log

enum class ProductType {
    VEGAN,
    NOT_VEGAN,
    DOUBTFUL,
    UNKNOWN,
    ;

    companion object {
        private const val TAG = "ProductType"
        fun fromString(value: String?): ProductType? {
            return runCatching {
                value?.let { ProductType.valueOf(it) }
            }.onFailure {
                Log.i(TAG, it.message ?: it.stackTraceToString())
            }.getOrNull()
        }
    }
}