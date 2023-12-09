package org.codingforanimals.veganuniverse.create.product.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
        coroutineScope {
            val id = product.id ?: throw IllegalStateException("Product ID cannot be null")
            val createDeferred = async { productService.create(product) }
            val addContributionDeferred = async {
                profileLookupsService.saveContent(
                    contentId = id,
                    saveableType = SaveableType.CONTRIBUTION,
                    contentType = SaveableContentType.PRODUCT,
                    userId = product.userId,
                )
            }
            awaitAll(createDeferred, addContributionDeferred)
        }
    }

}