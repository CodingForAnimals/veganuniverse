package org.codingforanimals.veganuniverse.additives.presentation.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import org.codingforanimals.veganuniverse.additives.domain.model.AdditiveType
import org.codingforanimals.veganuniverse.commons.designsystem.Doubtful
import org.codingforanimals.veganuniverse.commons.designsystem.Error
import org.codingforanimals.veganuniverse.commons.designsystem.NoData
import org.codingforanimals.veganuniverse.commons.designsystem.Success
import org.codingforanimals.veganuniverse.commons.ui.R.string.additive_confirmed_vegan_description
import org.codingforanimals.veganuniverse.commons.ui.R.string.additive_doubtful_vegan_description
import org.codingforanimals.veganuniverse.commons.ui.R.string.additive_nodata_vegan_description
import org.codingforanimals.veganuniverse.commons.ui.R.string.additive_not_vegan_description
import org.codingforanimals.veganuniverse.commons.ui.R.string.product_confirmed_vegan
import org.codingforanimals.veganuniverse.commons.ui.R.string.product_doubtful_vegan
import org.codingforanimals.veganuniverse.commons.ui.R.string.product_nodata
import org.codingforanimals.veganuniverse.commons.ui.R.string.product_not_vegan
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons

enum class AdditiveTypeUI(
    val icon: Icon.ImageDrawableResourceIcon,
    @StringRes val label: Int,
    @StringRes val description: Int,
    val color: Color,
) {
    VEGAN(
        icon = VUIcons.ProductConfirmedVegan,
        label = product_confirmed_vegan,
        description = additive_confirmed_vegan_description,
        color = Success,
    ),
    NOT_VEGAN(
        icon = VUIcons.ProductNotVegan,
        label = product_not_vegan,
        description = additive_not_vegan_description,
        color = Error,
    ),
    DOUBTFUL(
        icon = VUIcons.ProductDoubtfulVegan,
        label = product_doubtful_vegan,
        description = additive_doubtful_vegan_description,
        color = Doubtful,
    ),
    UNKNOWN(
        icon = VUIcons.ProductNoDataVegan,
        label = product_nodata,
        description = additive_nodata_vegan_description,
        color = NoData,
    ),
    ;

    companion object {
        fun fromString(value: String?): AdditiveTypeUI {
            return entries.firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}

internal fun AdditiveType.toUI() = AdditiveTypeUI.fromString(name)
