package org.codingforanimals.veganuniverse.product.domain.model

import android.util.Log
import org.codingforanimals.veganuniverse.commons.data.utils.accentInsensitive
import org.codingforanimals.veganuniverse.product.data.source.local.model.ProductEntity
import org.codingforanimals.veganuniverse.product.data.source.remote.model.ProductDTO
import org.codingforanimals.veganuniverse.product.domain.utils.getImageIdFromFullPath
import java.util.Date

internal fun Product.toDTO(): ProductDTO {
    return ProductDTO(
        objectKey = id,
        userId = userId,
        username = username,
        name = name,
        brand = brand,
        type = type.name,
        category = category.name,
        imageId = imageUrl?.getImageIdFromFullPath(),
        description = description,
        timestamp = null,
        lastUpdated = null,
        sourceUrl = sourceUrl,
    )
}

internal fun ProductDTO.toDomain(): Product {
    val name = checkNotNull(name) {
        "ProductDTO name cannot be null"
    }

    val brand = checkNotNull(brand) {
        "ProductDTO brand cannot be null"
    }

    val type = checkNotNull(ProductType.fromString(type)) {
        "ProductDTO type cannot be null"
    }

    val category = checkNotNull(ProductCategory.fromString(category)) {
        "ProductDTO category cannot be null"
    }

    val timestamp = checkNotNull(timestamp) {
        "ProductDTO timestamp cannot be null"
    }

    return Product(
        id = objectKey,
        userId = userId,
        username = username,
        name = name,
        brand = brand,
        description = description,
        type = type,
        category = category,
        createdAt = Date(timestamp),
        lastUpdatedAt = lastUpdated?.let { Date(it) },
        imageUrl = imageId,
        sourceUrl = sourceUrl,
    )
}

internal fun ProductEntity.toDomain(): Product {
    val type = checkNotNull(ProductType.fromString(type)) {
        "ProductEntity type cannot be null"
    }

    val category = checkNotNull(ProductCategory.fromString(category)) {
        "ProductEntity category cannot be null"
    }
    return Product(
        id = id,
        userId = userId,
        username = username,
        name = name,
        brand = brand,
        description = description,
        type = type,
        category = category,
        createdAt = Date(timestamp),
        lastUpdatedAt = lastUpdatedTimestamp?.let { Date(it) },
        imageUrl = imageUrl,
        sourceUrl = sourceUrl,
    )
}

internal fun Product.toEntity(): ProductEntity {
    val id = checkNotNull(id) {
        "Product id cannot be null"
    }

    val timestamp = checkNotNull(createdAt?.time) {
        "Product timestamp cannot be null"
    }
    return ProductEntity(
        id = id,
        userId = userId,
        username = username,
        name = name,
        nameAccentInsensitive = name.accentInsensitive(),
        brand = brand,
        brandAccentInsensitive = brand.accentInsensitive(),
        description = description,
        type = type.name,
        category = category.name,
        timestamp = timestamp,
        lastUpdatedTimestamp = lastUpdatedAt?.time,
        imageUrl = imageUrl,
        sourceUrl = sourceUrl,
    )
}
