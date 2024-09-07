package org.codingforanimals.veganuniverse.commons.product.presentation

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import org.codingforanimals.veganuniverse.commons.designsystem.Doubtful
import org.codingforanimals.veganuniverse.commons.designsystem.Error
import org.codingforanimals.veganuniverse.commons.designsystem.Success
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

data class ProductTypeUI(
    @StringRes val label: Int,
    val icon: Icon.ImageDrawableResourceIcon,
    @StringRes val description: Int,
    val color: Color,
    val shortLabel: String,
)

fun ProductType.toUI(): ProductTypeUI {
    return when (this) {
        ProductType.VEGAN -> ProductTypeUI(
            label = R.string.product_confirmed_vegan,
            icon = VUIcons.ProductConfirmedVegan,
            description = R.string.product_confirmed_vegan_description,
            color = Success,
            shortLabel = "Apto",
        )

        ProductType.NOT_VEGAN -> ProductTypeUI(
            label = R.string.product_not_vegan,
            icon = VUIcons.ProductNotVegan,
            description = R.string.product_not_vegan_description,
            color = Error,
            shortLabel = "No apto",
        )

        ProductType.DOUBTFUL -> ProductTypeUI(
            label = R.string.product_doubtful_vegan,
            icon = VUIcons.ProductDoubtfulVegan,
            description = R.string.product_doubtful_vegan_description,
            color = Doubtful,
            shortLabel = "Dudoso",
        )
    }
}
