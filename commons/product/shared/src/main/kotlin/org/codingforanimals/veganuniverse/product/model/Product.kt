package org.codingforanimals.veganuniverse.product.model

import java.util.Date

data class Product(
    val id: String?,
    val userId: String?,
    val username: String?,
    val name: String,
    val brand: String,
    val comment: String?,
    val type: ProductType,
    val category: ProductCategory,
    val createdAt: Date?,
    val imageUrl: String?,
    val validated: Boolean,
)
