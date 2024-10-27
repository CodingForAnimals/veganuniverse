package org.codingforanimals.veganuniverse.product.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(
    @PrimaryKey val id: String,
    val userId: String?,
    val username: String?,
    val name: String,
    val nameAccentInsensitive: String,
    val brand: String,
    val brandAccentInsensitive: String,
    val type: String,
    val category: String,
    val description: String?,
    val imageUrl: String?,
    val sourceUrl: String?,
    val timestamp: Long,
    val lastUpdatedTimestamp: Long?,
)