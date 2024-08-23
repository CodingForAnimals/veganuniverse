package org.codingforanimals.veganuniverse.product.presentation.model

import org.codingforanimals.veganuniverse.product.presentation.ProductCategoryUI
import org.codingforanimals.veganuniverse.product.presentation.ProductTypeUI
import java.util.Date

data class Product(
    val id: String?,
    val name: String,
    val brand: String,
    val comment: String?,
    val type: ProductTypeUI?,
    val category: ProductCategoryUI?,
    val userId: String?,
    val username: String?,
    val imageUrl: String?,
    val creationDate: Date?,
)
