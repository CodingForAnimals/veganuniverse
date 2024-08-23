package org.codingforanimals.veganuniverse.commons.product.presentation

import androidx.annotation.StringRes
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

data class ProductTypeUI(
    @StringRes val label: Int,
    val icon: Icon,
)

fun ProductType.toUI(): ProductTypeUI {
    return when (this) {
        ProductType.VEGAN -> ProductTypeUI(
            label = R.string.product_confirmed_vegan,
            icon = VUIcons.ProductConfirmedVegan
        )

        ProductType.NOT_VEGAN -> ProductTypeUI(
            label = R.string.product_not_vegan,
            icon = VUIcons.ProductNotVegan
        )

        ProductType.DOUBTFUL -> ProductTypeUI(
            label = R.string.product_doubtful_vegan,
            icon = VUIcons.ProductDoubtfulVegan
        )
    }
}
