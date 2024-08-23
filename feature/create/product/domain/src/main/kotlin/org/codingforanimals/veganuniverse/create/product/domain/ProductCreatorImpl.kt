package org.codingforanimals.veganuniverse.create.product.domain

import org.codingforanimals.veganuniverse.product.entity.Product
import org.codingforanimals.veganuniverse.product.services.firebase.ProductService
import org.codingforanimals.veganuniverse.profile.model.SaveableContentType
import org.codingforanimals.veganuniverse.profile.model.SaveableType
import org.codingforanimals.veganuniverse.profile.services.firebase.ProfileLookupsService

internal class ProductCreatorImpl(
    private val productService: ProductService,
    private val profileLookupsService: ProfileLookupsService,
) : ProductCreator {
    override suspend fun submitProduct(product: Product) {
        val productId = productService.create(product)
        profileLookupsService.saveContent(
            contentId = productId,
            saveableType = SaveableType.CONTRIBUTION,
            contentType = SaveableContentType.PRODUCT,
            userId = product.userId,
        )
    }
}