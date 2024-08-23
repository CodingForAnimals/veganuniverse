package org.codingforanimals.veganuniverse.create.product.domain.model

import android.os.Parcelable
import org.codingforanimals.veganuniverse.product.model.ProductCategory
import org.codingforanimals.veganuniverse.product.model.ProductType

data class _ProductForm(
    val name: String,
    val brand: String,
    val category: String,
    val type: String,
    val comment: String?,
    val imageModel: Parcelable,
)

data class ProductForm(
    val name: String,
    val brand: String,
    val category: ProductCategory,
    val type: ProductType,
    val comment: String?,
    val imageModel: Parcelable,
)
