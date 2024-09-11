package org.codingforanimals.veganuniverse.commons.product.shared.model

import android.util.Log
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.ADDITIVES
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.ALCOHOLIC_BEVERAGES
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.BAKED_GOODS
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.BEVERAGES
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.CHOCOLATES
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.CLEANING_HYGIENE
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.COOKIES
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.COSMETICS
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.DOUGH_DISCS
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.DRESSINGS_SAUCES
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.NOT_DAIRY
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.PERSONAL_CARE
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.SALTY_SNACKS
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.SWEET_SNACKS
import org.codingforanimals.veganuniverse.commons.product.shared.model.DeprecatedProductCategory.VITAMINS_AND_SUPPLEMENTS

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
    ADDITIVE,
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

        private fun fromDeprecated(deprecatedProductCategory: DeprecatedProductCategory?): ProductCategory? {
            return when (deprecatedProductCategory) {
                CHOCOLATES -> CHOCOLATE
                COOKIES -> COOKIE
                SWEET_SNACKS -> CANDY
                SALTY_SNACKS -> SNACK
                BAKED_GOODS -> BAKED
                DeprecatedProductCategory.PASTA -> PASTA
                DeprecatedProductCategory.FROZEN -> FROZEN
                DeprecatedProductCategory.SHELF -> SHELF
                DRESSINGS_SAUCES -> DRESSING_OR_SAUCE
                DeprecatedProductCategory.SPREADABLE -> SPREADABLE
                NOT_DAIRY -> VEGETABLE_MILK
                BEVERAGES -> BEVERAGE
                ALCOHOLIC_BEVERAGES -> BEVERAGE
                DOUGH_DISCS -> PASTA
                PERSONAL_CARE -> HYGIENE
                COSMETICS -> COSMETIC
                CLEANING_HYGIENE -> CLEANING
                VITAMINS_AND_SUPPLEMENTS -> SUPPLEMENT
                ADDITIVES -> ADDITIVE
                DeprecatedProductCategory.OTHER -> OTHER
                else -> null
            }
        }
    }
}

private enum class DeprecatedProductCategory {
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
        private const val TAG = "DeprecatedProductCategory"
        fun fromString(value: String?): DeprecatedProductCategory? {
            return runCatching {
                value?.let { DeprecatedProductCategory.valueOf(it) }
            }.onFailure {
                Log.i(TAG, it.message ?: it.stackTraceToString())
            }.getOrNull()
        }
    }
}