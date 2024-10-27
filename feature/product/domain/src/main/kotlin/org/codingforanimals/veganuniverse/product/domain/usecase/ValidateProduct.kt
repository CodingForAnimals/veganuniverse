package org.codingforanimals.veganuniverse.product.domain.usecase

import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository

class ValidateProduct(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(product: Product): Result<String> {
        return runCatching {
            productRepository.validateProduct(product)
        }.onFailure {
            Analytics.logNonFatalException(it)
        }
    }
}

