package org.codingforanimals.veganuniverse.create.product.domain.model

import android.os.Parcelable

data class ProductForm(
    val name: String,
    val brand: String,
    val category: String,
    val type: String,
    val comment: String?,
    val imageModel: Parcelable,
)
