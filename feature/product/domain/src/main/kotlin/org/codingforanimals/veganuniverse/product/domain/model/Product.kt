package org.codingforanimals.veganuniverse.product.domain.model

import java.util.Date

data class Product(
    val id: String? = null,
    val name: String,
    val brand: String,
    val comment: String?,
    val type: String,
    val category: String,
    val userId: String,
    val createdAt: Date? = null,
    val imageStorageRef: String? = null,
)
