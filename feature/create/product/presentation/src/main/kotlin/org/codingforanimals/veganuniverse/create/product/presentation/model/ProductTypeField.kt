package org.codingforanimals.veganuniverse.create.product.presentation.model

import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.viewmodel.ValidationField

data class ProductTypeField(
    val type: ProductType? = null,
) : ValidationField() {
    override val isValid: Boolean
        get() = type != null
}

data class ProductCategoryField(
    val category: ProductCategory? = null,
) : ValidationField() {
    override val isValid: Boolean
        get() = category != null
}