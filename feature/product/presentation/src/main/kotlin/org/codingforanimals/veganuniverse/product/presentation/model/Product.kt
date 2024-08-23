package org.codingforanimals.veganuniverse.product.presentation.model

import java.util.Date
import org.codingforanimals.veganuniverse.product.ui.ProductCategory
import org.codingforanimals.veganuniverse.product.ui.ProductType

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
