package org.codingforanimals.veganuniverse.create.product.presentation.model

import org.codingforanimals.veganuniverse.product.model.ProductCategory
import org.codingforanimals.veganuniverse.product.model.ProductType
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