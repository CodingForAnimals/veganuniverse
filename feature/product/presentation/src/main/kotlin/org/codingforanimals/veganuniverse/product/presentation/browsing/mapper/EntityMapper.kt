package org.codingforanimals.veganuniverse.product.presentation.browsing.mapper

import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.codingforanimals.veganuniverse.product.presentation.toUI
import org.codingforanimals.veganuniverse.product.model.Product as ProductDomainModel

internal fun ProductDomainModel.toView(): Product {
    return Product(
        id = id,
        name = name,
        brand = brand,
        comment = comment,
        type = type.toUI(),
        category = category.toUI(),
        userId = userId,
        username = username,
        imageUrl = imageUrl,
        creationDate = createdAt,
    )
}