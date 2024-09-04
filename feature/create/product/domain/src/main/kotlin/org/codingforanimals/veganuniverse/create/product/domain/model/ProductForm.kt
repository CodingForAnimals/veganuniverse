package org.codingforanimals.veganuniverse.create.product.domain.model

import android.os.Parcelable
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType

data class ProductForm(
    val name: String,
    val brand: String?,
    val category: ProductCategory,
    val type: ProductType,
    val comment: String?,
    val imageModel: Parcelable?,
)
