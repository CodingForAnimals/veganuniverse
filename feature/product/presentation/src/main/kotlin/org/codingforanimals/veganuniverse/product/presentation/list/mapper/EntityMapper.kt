package org.codingforanimals.veganuniverse.product.presentation.list.mapper

import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.codingforanimals.veganuniverse.product.ui.ProductCategory
import org.codingforanimals.veganuniverse.product.ui.ProductType
import org.codingforanimals.veganuniverse.product.domain.model.Product as ProductDomainModel

internal fun ProductDomainModel.toViewModel(): Product {
    return Product(
        id = id,
        name = name,
        brand = brand,
        comment = comment,
        type = ProductType.fromString(type),
        category = ProductCategory.fromString(category),
        userId = userId,
        imageUrl = imageStorageRef,
        creationDate = createdAt,
    )
}