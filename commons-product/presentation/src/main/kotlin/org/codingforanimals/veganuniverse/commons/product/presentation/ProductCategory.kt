package org.codingforanimals.veganuniverse.commons.product.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.ADDITIVES
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.ALCOHOLIC_BEVERAGES
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.BAKED_GOODS
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.BEVERAGES
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.CHOCOLATES
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.CLEANING_HYGIENE
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.COOKIES
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.COSMETICS
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.DOUGH_DISCS
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.DRESSINGS_SAUCES
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.FROZEN
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.NOT_DAIRY
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.OTHER
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.PASTA
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.PERSONAL_CARE
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.SALTY_SNACKS
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.SHELF
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.SPREADABLE
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.SWEET_SNACKS
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.VITAMINS_AND_SUPPLEMENTS

data class ProductCategoryUI(
    @StringRes val label: Int,
    @DrawableRes val imageRef: Int,
)

fun ProductCategory.toUI(): ProductCategoryUI {
    return when (this) {
        CHOCOLATES -> ProductCategoryUI(R.string.chocolates, R.drawable.img_chocolates)
        COOKIES -> ProductCategoryUI(R.string.cookies, R.drawable.img_cookies)
        SWEET_SNACKS -> ProductCategoryUI(R.string.sweet_snacks, R.drawable.img_sweet_snacks)
        SALTY_SNACKS -> ProductCategoryUI(R.string.salty_snacks, R.drawable.img_salty_snacks)
        BAKED_GOODS -> ProductCategoryUI(R.string.baked_goods, R.drawable.img_baked_goods)
        PASTA -> ProductCategoryUI(R.string.pasta, R.drawable.img_pasta)
        FROZEN -> ProductCategoryUI(R.string.frozen, R.drawable.img_frozen)
        SHELF -> ProductCategoryUI(R.string.shelf, R.drawable.img_shelf)
        DRESSINGS_SAUCES -> ProductCategoryUI(
            R.string.dressings_sauces,
            R.drawable.img_dressings_sauces
        )

        SPREADABLE -> ProductCategoryUI(R.string.spreadable, R.drawable.img_spreadables)
        NOT_DAIRY -> ProductCategoryUI(R.string.not_dairy, R.drawable.img_not_dairy)
        BEVERAGES -> ProductCategoryUI(R.string.beverages, R.drawable.img_beverages)
        ALCOHOLIC_BEVERAGES -> ProductCategoryUI(
            R.string.alcoholic_beverages,
            R.drawable.img_alcoholic_beverages
        )

        DOUGH_DISCS -> ProductCategoryUI(R.string.dough_discs, R.drawable.img_dough_disks)
        PERSONAL_CARE -> ProductCategoryUI(R.string.personal_care, R.drawable.img_personal_care)
        COSMETICS -> ProductCategoryUI(R.string.cosmetics, R.drawable.img_cosmetics)
        CLEANING_HYGIENE -> ProductCategoryUI(
            R.string.cleaning_hygiene,
            R.drawable.img_cleaning_hygiene
        )

        VITAMINS_AND_SUPPLEMENTS -> ProductCategoryUI(
            R.string.vitamins_and_supplements,
            R.drawable.img_vitamins_and_supplements
        )

        ADDITIVES -> ProductCategoryUI(R.string.additives, R.drawable.img_additives)
        OTHER -> ProductCategoryUI(R.string.other, R.drawable.img_others)
    }
}
