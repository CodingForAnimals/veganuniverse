package org.codingforanimals.veganuniverse.product.model

import android.util.Log

enum class ProductCategory {
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
    ADDITIVES,
    OTHER,
    ;

    companion object {
        private const val TAG = "ProductCategory"
        fun fromString(value: String?): ProductCategory? {
            return runCatching {
                value?.let { ProductCategory.valueOf(it) }
            }.onFailure {
                Log.e(TAG, it.stackTraceToString())
            }.getOrNull()
        }
    }
}