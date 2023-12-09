package org.codingforanimals.veganuniverse.product.ui

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.ui.icon.Icon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

enum class ProductType(
    @StringRes val label: Int,
    val icon: Icon,
) {
    VEGAN(R.string.product_confirmed_vegan, VUIcons.ProductConfirmedVegan),
    NOT_VEGAN(R.string.product_not_vegan, VUIcons.ProductNotVegan),
    DOUBTFUL(R.string.product_doubtful_vegan, VUIcons.ProductDoubtfulVegan),
}