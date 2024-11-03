package org.codingforanimals.veganuniverse.product.domain.model

import java.util.Date

data class ProductEdit(
    val id: String?,
    val originalId: String,
    val editUserId: String?,
    val editUsername: String?,
    val userId: String?,
    val username: String?,
    val name: String,
    val brand: String,
    val description: String?,
    val type: ProductType,
    val category: ProductCategory,
    val createdAt: Date?,
    val lastUpdatedAt: Date?,
    val imageUrl: String?,
    val updatesImage: Boolean,
    val sourceUrl: String?,
)
