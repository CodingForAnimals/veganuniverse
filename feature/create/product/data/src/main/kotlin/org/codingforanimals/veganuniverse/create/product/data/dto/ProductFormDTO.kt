package org.codingforanimals.veganuniverse.create.product.data.dto

import android.os.Parcelable

data class ProductFormDTO(
    val userId: String,
    val name: String,
    val brand: String,
    val category: String,
    val type: String,
    val comment: String?,
    val imageModel: Parcelable,
    val keywords: List<String>,
)
