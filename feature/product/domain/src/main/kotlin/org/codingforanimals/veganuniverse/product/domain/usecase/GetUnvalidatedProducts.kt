package org.codingforanimals.veganuniverse.product.domain.usecase

import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository

class GetUnvalidatedProducts(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(): List<Product> {
        return try {
            productRepository.getUnvalidatedProductsFromRemote()
        } catch (e: Throwable) {
            Analytics.logNonFatalException(e)
            throw e
        }
    }
}
