package org.codingforanimals.veganuniverse.commons.product.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory

val Product.resolveBrand: String
    @Composable
    get() = when (category) {
        ProductCategory.ADDITIVE -> comment ?: stringResource(id = R.string.additive)
        else -> brand.orEmpty()
    }