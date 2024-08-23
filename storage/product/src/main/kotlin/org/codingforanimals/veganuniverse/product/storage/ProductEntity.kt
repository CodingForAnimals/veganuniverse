package org.codingforanimals.veganuniverse.product.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val brand: String,
    val comments: String?,
    val type: String,
    val category: String,
    val userId: String,
    val username: String,
    val createdAt: String? = null,
)


