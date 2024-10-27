package org.codingforanimals.veganuniverse.product.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.ALFAJOR
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.BAKED
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.BEVERAGE
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.CANDY
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.CHOCOLATE
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.CLEANING
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.COOKIE
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.COSMETIC
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.DRESSING_OR_SAUCE
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.FROZEN
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.HYGIENE
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.ICE_CREAM
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.OTHER
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.PASTA
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.SHELF
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.SNACK
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.SPREADABLE
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.SUPPLEMENT
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.VEGETABLE_CHEESE
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.VEGETABLE_MILK
import org.codingforanimals.veganuniverse.product.domain.model.ProductCategory.YOGURT
import org.codingforanimals.veganuniverse.product.presentation.R

data class ProductCategoryUI(
    @StringRes val label: Int,
    @DrawableRes val imageRef: Int,
)

fun ProductCategory.toUI(): ProductCategoryUI {
    return when (this) {
        ALFAJOR -> ProductCategoryUI(
            label = R.string.product_category_label_alfajor,
            imageRef = R.drawable.img_alfajor
        )

        CHOCOLATE -> ProductCategoryUI(
            label = R.string.product_category_label_chocolate,
            imageRef = R.drawable.img_chocolates
        )

        CANDY -> ProductCategoryUI(
            label = R.string.product_category_candy,
            imageRef = R.drawable.img_candy
        )

        COOKIE -> ProductCategoryUI(
            label = R.string.product_category_label_cookie,
            imageRef = R.drawable.img_cookie
        )

        VEGETABLE_MILK -> ProductCategoryUI(
            label = R.string.product_category_label_vegetable_milk,
            imageRef = R.drawable.img_vegetable_milk
        )

        VEGETABLE_CHEESE -> ProductCategoryUI(
            label = R.string.product_category_label_vegetable_cheese,
            imageRef = R.drawable.img_vegetable_cheese
        )

        YOGURT -> ProductCategoryUI(
            label = R.string.product_category_label_yogurt,
            imageRef = R.drawable.img_yogurt
        )

        ICE_CREAM -> ProductCategoryUI(
            label = R.string.product_category_label_ice_cream,
            imageRef = R.drawable.img_ice_cream
        )

        BAKED -> ProductCategoryUI(
            label = R.string.product_category_label_baked,
            imageRef = R.drawable.img_baked
        )

        SNACK -> ProductCategoryUI(
            label = R.string.product_category_label_snack,
            imageRef = R.drawable.img_snack
        )

        DRESSING_OR_SAUCE -> ProductCategoryUI(
            label = R.string.product_category_label_dressing_or_sauce,
            imageRef = R.drawable.img_dressing_or_sauce
        )

        BEVERAGE -> ProductCategoryUI(
            label = R.string.product_category_label_beverage,
            imageRef = R.drawable.img_beverage
        )

        SUPPLEMENT -> ProductCategoryUI(
            label = R.string.product_category_label_supplement,
            imageRef = R.drawable.img_supplement
        )

        HYGIENE -> ProductCategoryUI(
            label = R.string.product_category_label_hygiene,
            imageRef = R.drawable.img_hygiene
        )

        COSMETIC -> ProductCategoryUI(
            label = R.string.product_category_label_cosmetic,
            imageRef = R.drawable.img_cosmetic
        )

        CLEANING -> ProductCategoryUI(
            label = R.string.product_category_label_cleaning,
            imageRef = R.drawable.img_cleaning
        )

        FROZEN -> ProductCategoryUI(
            label = R.string.product_category_label_frozen,
            imageRef = R.drawable.img_frozen
        )

        PASTA -> ProductCategoryUI(
            label = R.string.product_category_label_pasta,
            imageRef = R.drawable.img_pasta
        )

        SPREADABLE -> ProductCategoryUI(
            label = R.string.product_category_label_spreadable,
            imageRef = R.drawable.img_spreadable
        )

        SHELF -> ProductCategoryUI(
            label = R.string.product_category_label_shelf,
            imageRef = R.drawable.img_shelf
        )

        OTHER -> ProductCategoryUI(
            label = R.string.product_category_label_other,
            imageRef = R.drawable.img_other
        )
    }
}
