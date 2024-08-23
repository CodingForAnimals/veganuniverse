package org.codingforanimals.veganuniverse.product.entity

data class Product(
    val id: String? = null,
    val name: String,
    val brand: String,
    val comments: String?,
    val type: String,
    val category: String,
    val userId: String,
    val username: String,
    val createdAt: String? = null,
)
