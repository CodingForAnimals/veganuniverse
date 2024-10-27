package org.codingforanimals.veganuniverse.product.domain.model

import android.util.Log

enum class ProductCategory {
    ALFAJOR,
    CHOCOLATE,
    CANDY,
    COOKIE,
    VEGETABLE_MILK,
    VEGETABLE_CHEESE,
    YOGURT,
    ICE_CREAM,
    FROZEN,
    PASTA,
    BAKED,
    SPREADABLE,
    SNACK,
    DRESSING_OR_SAUCE,
    SHELF,
    BEVERAGE,
    SUPPLEMENT,
    HYGIENE,
    COSMETIC,
    CLEANING,
    OTHER,
    ;

    companion object {
        private const val TAG = "ProductCategory"
        fun fromString(value: String?): ProductCategory? {
            return runCatching {
                value?.let { ProductCategory.valueOf(it) }
            }.onFailure {
                Log.i(TAG, it.message ?: it.stackTraceToString())
            }.getOrNull()
        }

    }
}
