package org.codingforanimals.veganuniverse.product.presentation.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import org.codingforanimals.veganuniverse.commons.designsystem.Doubtful
import org.codingforanimals.veganuniverse.commons.designsystem.Error
import org.codingforanimals.veganuniverse.commons.designsystem.NoData
import org.codingforanimals.veganuniverse.commons.designsystem.Success
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.product.domain.model.ProductType
import org.codingforanimals.veganuniverse.product.presentation.R

enum class ProductTypeUI(
    @StringRes val label: Int,
    val icon: Icon.ImageDrawableResourceIcon,
    @StringRes val description: Int,
    val color: Color,
    val shortLabel: String,
) {
    VEGAN(
        label = R.string.product_confirmed_vegan,
        icon = VUIcons.ProductConfirmedVegan,
        description = R.string.product_confirmed_vegan_description,
        color = Success,
        shortLabel = "Apto",
    ),
    NOT_VEGAN(
        label = R.string.product_not_vegan,
        icon = VUIcons.ProductNotVegan,
        description = R.string.product_not_vegan_description,
        color = Error,
        shortLabel = "No apto",
    ),
    DOUBTFUL(
        label = R.string.product_doubtful_vegan,
        icon = VUIcons.ProductDoubtfulVegan,
        description = R.string.product_doubtful_vegan_description,
        color = Doubtful,
        shortLabel = "Dudoso",
    ),
    UNKNOWN(
        label = R.string.product_unknown_vegan,
        icon = VUIcons.ProductNoDataVegan,
        description = R.string.product_unknown_vegan_description,
        color = NoData,
        shortLabel = "Sin datos",
    )
}

fun ProductType.toUI(): ProductTypeUI = when (this) {
    ProductType.VEGAN -> ProductTypeUI.VEGAN
    ProductType.NOT_VEGAN -> ProductTypeUI.NOT_VEGAN
    ProductType.DOUBTFUL -> ProductTypeUI.DOUBTFUL
    ProductType.UNKNOWN -> ProductTypeUI.UNKNOWN
}
