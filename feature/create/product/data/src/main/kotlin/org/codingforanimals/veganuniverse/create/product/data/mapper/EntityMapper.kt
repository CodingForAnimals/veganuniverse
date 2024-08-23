package org.codingforanimals.veganuniverse.create.product.data.mapper

import org.codingforanimals.veganuniverse.create.product.data.dto.ProductFormDTO
import org.codingforanimals.veganuniverse.create.product.data.dto.ProductFormFirestoreDTO

internal fun ProductFormDTO.toFirebaseModel(imageStorageRef: String): ProductFormFirestoreDTO {
    return ProductFormFirestoreDTO(
        userId = userId,
        name = name,
        brand = brand,
        category = category,
        type = type,
        comment = comment,
        imageStorageRef = imageStorageRef,
        keywords = keywords
    )
}