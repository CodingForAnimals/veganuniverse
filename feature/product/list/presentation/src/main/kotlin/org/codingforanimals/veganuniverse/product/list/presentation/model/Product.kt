package org.codingforanimals.veganuniverse.product.list.presentation.model

import org.codingforanimals.veganuniverse.product.ui.ProductCategory
import org.codingforanimals.veganuniverse.product.ui.ProductType
import java.util.Date

data class Product(
    val id: String?,
    val name: String,
    val brand: String,
    val comment: String?,
    val type: ProductType?,
    val category: ProductCategory?,
    val userId: String,
    val imageUrl: String?,
    val creationDate: Date?,
)
