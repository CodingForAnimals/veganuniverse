package org.codingforanimals.veganuniverse.commons.product.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory.*

data class ProductCategoryUI(
    @StringRes val label: Int,
    @DrawableRes val imageRef: Int,
)

fun ProductCategory.toUI(): ProductCategoryUI {
    return when (this) {
        ALFAJOR -> ProductCategoryUI(R.string.product_category_label_alfajor, R.drawable.img_alfajor)
        CHOCOLATE -> ProductCategoryUI(R.string.product_category_label_chocolate, R.drawable.img_chocolates)
        CANDY -> ProductCategoryUI(R.string.product_category_candy, R.drawable.img_candy)
        COOKIE -> ProductCategoryUI(R.string.product_category_label_cookie, R.drawable.img_cookie)
        VEGETABLE_MILK -> ProductCategoryUI(R.string.product_category_label_vegetable_milk, R.drawable.img_vegetable_milk)
        VEGETABLE_CHEESE -> ProductCategoryUI(R.string.product_category_label_vegetable_cheese, R.drawable.img_vegetable_cheese)
        YOGURT -> ProductCategoryUI(R.string.product_category_label_yogurt, R.drawable.img_yogurt)
        ICE_CREAM -> ProductCategoryUI(R.string.product_category_label_ice_cream, R.drawable.img_ice_cream)
        BAKED -> ProductCategoryUI(R.string.product_category_label_baked, R.drawable.img_baked)
        SNACK -> ProductCategoryUI(R.string.product_category_label_snack, R.drawable.img_snack)
        DRESSING_OR_SAUCE -> ProductCategoryUI(R.string.product_category_label_dressing_or_sauce, R.drawable.img_dressing_or_sauce)
        BEVERAGE -> ProductCategoryUI(R.string.product_category_label_beverage, R.drawable.img_beverage)
        SUPPLEMENT -> ProductCategoryUI(R.string.product_category_label_supplement, R.drawable.img_supplement)
        HYGIENE -> ProductCategoryUI(R.string.product_category_label_hygiene, R.drawable.img_hygiene)
        COSMETIC -> ProductCategoryUI(R.string.product_category_label_cosmetic, R.drawable.img_cosmetic)
        CLEANING -> ProductCategoryUI(R.string.product_category_label_cleaning, R.drawable.img_cleaning)
        FROZEN -> ProductCategoryUI(R.string.product_category_label_frozen, R.drawable.img_frozen)
        PASTA -> ProductCategoryUI(R.string.product_category_label_pasta, R.drawable.img_pasta)
        SPREADABLE -> ProductCategoryUI(R.string.product_category_label_spreadable, R.drawable.img_spreadable)
        SHELF -> ProductCategoryUI(R.string.product_category_label_shelf, R.drawable.img_shelf)
        OTHER -> ProductCategoryUI(R.string.product_category_label_other, R.drawable.img_other)
    }
}
