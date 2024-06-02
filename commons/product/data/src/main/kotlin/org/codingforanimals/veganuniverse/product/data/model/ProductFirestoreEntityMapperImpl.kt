package org.codingforanimals.veganuniverse.product.data.model

import org.codingforanimals.veganuniverse.firebase.storage.model.PublicImageApi
import org.codingforanimals.veganuniverse.product.data.source.ProductFirestoreDataSource.Companion.BASE_PRODUCT_PICTURE_PATH
import org.codingforanimals.veganuniverse.product.model.Product
import org.codingforanimals.veganuniverse.product.model.ProductCategory
import org.codingforanimals.veganuniverse.product.model.ProductType

internal class ProductFirestoreEntityMapperImpl(
    private val publicImageApi: PublicImageApi,
) : ProductFirestoreEntityMapper {
    override fun mapToModel(firestoreEntity: ProductFirestoreEntity): Product =
        with(firestoreEntity) {
            Product(
                id = id,
                userId = userId,
                username = username,
                name = name.orEmpty(),
                brand = brand.orEmpty(),
                comment = comment,
                type = ProductType.fromString(type) ?: ProductType.DOUBTFUL,
                category = ProductCategory.fromString(category) ?: ProductCategory.OTHER,
                createdAt = createdAt?.toDate(),
                imageUrl = imageId?.let {
                    publicImageApi.getFilePath(
                        path = BASE_PRODUCT_PICTURE_PATH,
                        imageId = it,
                    )
                },
                validated = validated,
            )
        }
}