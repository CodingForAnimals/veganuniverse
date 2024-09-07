package org.codingforanimals.veganuniverse.commons.product.shared.model

import android.util.Log

enum class ProductCategory {
    ADDITIVES,
    CHOCOLATES,
    COOKIES,
    SWEET_SNACKS,
    SALTY_SNACKS,
    BAKED_GOODS,
    PASTA,
    FROZEN,
    SHELF,
    DRESSINGS_SAUCES,
    SPREADABLE,
    NOT_DAIRY,
    BEVERAGES,
    ALCOHOLIC_BEVERAGES,
    DOUGH_DISCS,
    PERSONAL_CARE,
    COSMETICS,
    CLEANING_HYGIENE,
    VITAMINS_AND_SUPPLEMENTS,
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