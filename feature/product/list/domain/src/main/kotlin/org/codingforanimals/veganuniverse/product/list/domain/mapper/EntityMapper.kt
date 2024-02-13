package org.codingforanimals.veganuniverse.product.list.domain.mapper

import org.codingforanimals.veganuniverse.product.list.data.model.ProductEntity
import org.codingforanimals.veganuniverse.product.list.domain.model.Product

fun ProductEntity.toDomainModel(): Product {
    return Product(
        id = id,
        name = name,
        brand = brand,
        comment = comment,
        type = type,
        category = category,
        userId = userId,
        username = username,
        createdAt = date,
        imageStorageRef = imageStorageRef,
    )
}