package org.codingforanimals.veganuniverse.product.presentation

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.product.model.ProductType
import org.codingforanimals.veganuniverse.ui.R.string.product_confirmed_vegan
import org.codingforanimals.veganuniverse.ui.R.string.product_doubtful_vegan
import org.codingforanimals.veganuniverse.ui.R.string.product_not_vegan
import org.codingforanimals.veganuniverse.ui.icon.Icon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons

data class ProductTypeUI(
    @StringRes val label: Int,
    val icon: Icon,
)

fun ProductType.toUI(): ProductTypeUI {
    return when (this) {
        ProductType.VEGAN -> ProductTypeUI(product_confirmed_vegan, VUIcons.ProductConfirmedVegan)
        ProductType.NOT_VEGAN -> ProductTypeUI(product_not_vegan, VUIcons.ProductNotVegan)
        ProductType.DOUBTFUL -> ProductTypeUI(product_doubtful_vegan, VUIcons.ProductDoubtfulVegan)
    }
}
