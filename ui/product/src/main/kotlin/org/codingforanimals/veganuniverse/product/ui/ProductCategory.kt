package org.codingforanimals.veganuniverse.product.ui

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class ProductCategory(
    @StringRes val label: Int,
    @DrawableRes val imageRef: Int,
) {
    CHOCOLATES(R.string.chocolates, R.drawable.img_beverages),
    COOKIES(R.string.cookies, R.drawable.img_cookies),
    SWEET_SNACKS(R.string.sweet_snacks, R.drawable.img_beverages),
    SALTY_SNACKS(R.string.salty_snacks, R.drawable.img_beverages),
    BAKED_GOODS(R.string.baked_goods, R.drawable.img_beverages),
    PASTA(R.string.pasta, R.drawable.img_beverages),
    FROZEN(R.string.frozen, R.drawable.img_beverages),
    SHELF(R.string.shelf, R.drawable.img_shelf),
    DRESSINGS_SAUCES(R.string.dressings_sauces, R.drawable.img_dressings_sauces),
    SPREADABLE(R.string.spreadable, R.drawable.img_dressings_sauces),
    NOT_DAIRY(R.string.not_dairy, R.drawable.img_not_dairy),
    BEVERAGES(R.string.beverages, R.drawable.img_beverages),
    ALCOHOLIC_BEVERAGES(R.string.alcoholic_beverages, R.drawable.img_cleaning_hygiene),
    DOUGH_DISCS(R.string.dough_discs, R.drawable.img_beverages),
    PERSONAL_CARE(R.string.personal_care, R.drawable.img_beverages),
    COSMETICS(R.string.cosmetics, R.drawable.img_cosmetics),
    CLEANING_HYGIENE(R.string.cleaning_hygiene, R.drawable.img_cleaning_hygiene),
    VITAMINS_AND_SUPPLEMENTS(R.string.vitamins_and_supplements, R.drawable.img_cleaning_hygiene),
    ADDITIVES(R.string.additives, R.drawable.img_cleaning_hygiene),
    OTHER(R.string.other, R.drawable.img_cleaning_hygiene),
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
