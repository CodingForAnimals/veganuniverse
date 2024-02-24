package org.codingforanimals.veganuniverse.product.domain.mapper

import org.codingforanimals.veganuniverse.product.data.model.Product as ProductDataModel
import org.codingforanimals.veganuniverse.product.domain.model.Product as ProductDomainModel

fun ProductDataModel.toDomainModel(): ProductDomainModel {
    return ProductDomainModel(
        id = id,
        name = name,
        brand = brand,
        comment = comment,
        type = type,
        category = category,
        userId = userId,
        createdAt = date,
        imageStorageRef = imageStorageRef,
    )
}
