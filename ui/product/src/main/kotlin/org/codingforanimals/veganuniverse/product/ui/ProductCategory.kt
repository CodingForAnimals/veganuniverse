package org.codingforanimals.veganuniverse.product.ui

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class ProductCategory(
    @StringRes val label: Int,
    @DrawableRes val imageRef: Int,
) {
    BEVERAGES(R.string.beverages, R.drawable.img_beverages),
    COOKIES(R.string.cookies, R.drawable.img_cookies),
    NOT_DAIRY(R.string.not_dairy, R.drawable.img_not_dairy),
    SHELF(R.string.shelf, R.drawable.img_shelf),
    DRESSINGS_SAUCES(R.string.dressings_sauces, R.drawable.img_dressings_sauces),
    CLEANING_HYGIENE(R.string.cleaning_hygiene, R.drawable.img_cleaning_hygiene),
    COSMETICS(R.string.cosmetics, R.drawable.img_cosmetics),
    ;

    companion object {
        private const val TAG = "ProductCategory"
        fun fromString(value: String): ProductCategory? {
            return runCatching {
                ProductCategory.valueOf(value)
            }.onFailure {
                Log.e(TAG, it.stackTraceToString())
            }.getOrNull()
        }
    }
}
