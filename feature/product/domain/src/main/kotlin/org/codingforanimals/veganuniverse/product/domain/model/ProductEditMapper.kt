package org.codingforanimals.veganuniverse.product.domain.model

import org.codingforanimals.veganuniverse.product.data.source.remote.model.ProductEditDTO
import org.codingforanimals.veganuniverse.product.domain.utils.getImageIdFromFullPath
import java.util.Date

internal fun ProductEdit.toDTO(): ProductEditDTO {
    return ProductEditDTO(
        objectKey = id,
        originalKey = originalId,
        editUserId = editUserId,
        editUsername = editUsername,
        userId = userId,
        username = username,
        name = name,
        brand = brand,
        type = type.name,
        category = category.name,
        imageId = imageUrl?.getImageIdFromFullPath(),
        description = description,
        timestamp = createdAt?.time,
        lastUpdated = lastUpdatedAt?.time,
        sourceUrl = sourceUrl
    )
}

internal fun ProductEditDTO.toDomain(): ProductEdit {
    val originalKey = checkNotNull(originalKey) {
        "ProductEditDTO.originalKey cannot be null"
    }
    val name = checkNotNull(name) {
        "ProductEditDTO.name cannot be null"
    }
    val brand = checkNotNull(brand) {
        "ProductEditDTO.brand cannot be null"
    }
    val type = checkNotNull(ProductType.fromString(type)) {
        "ProductEditDTO.type cannot be mapped to null"
    }
    val category = checkNotNull(ProductCategory.fromString(category)) {
        "ProductEditDTO.category cannot be mapped to null"
    }
    val timestamp = checkNotNull(timestamp) {
        "ProductEditDTO.timestamp cannot be null"
    }
    return ProductEdit(
        id = objectKey,
        originalId = originalKey,
        editUserId = editUserId,
        editUsername = editUsername,
        userId = userId,
        username = username,
        name = name,
        brand = brand,
        type = type,
        category = category,
        imageUrl = imageId,
        description = description,
        createdAt = Date(timestamp),
        lastUpdatedAt = lastUpdated?.let { Date(it) },
        sourceUrl = sourceUrl
    )
}
