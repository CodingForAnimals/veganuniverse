package org.codingforanimals.veganuniverse.product.presentation.model

import org.codingforanimals.veganuniverse.commons.product.presentation.ProductCategoryUI
import org.codingforanimals.veganuniverse.commons.product.presentation.ProductTypeUI
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product as ProductModel
import java.util.Date

internal data class Product(
    val id: String?,
    val name: String,
    val brand: String,
    val comment: String?,
    val type: ProductTypeUI,
    val category: ProductCategoryUI,
    val userId: String?,
    val username: String?,
    val imageUrl: String?,
    val createdAt: Date?,
)

internal fun ProductModel.toView(): Product {
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
        createdAt = createdAt,
    )
}
