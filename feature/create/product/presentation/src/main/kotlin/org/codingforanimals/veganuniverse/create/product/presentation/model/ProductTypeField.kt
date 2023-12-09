package org.codingforanimals.veganuniverse.create.product.presentation.model

import org.codingforanimals.veganuniverse.product.ui.ProductCategory
import org.codingforanimals.veganuniverse.product.ui.ProductType
import org.codingforanimals.veganuniverse.ui.viewmodel.ValidationField

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