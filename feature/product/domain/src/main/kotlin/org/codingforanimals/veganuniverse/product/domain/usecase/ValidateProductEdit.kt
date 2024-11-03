package org.codingforanimals.veganuniverse.product.domain.usecase

import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.product.domain.model.ProductEdit
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository

class ValidateProductEdit(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(product: ProductEdit): Result<Unit> {
        return runCatching {
            productRepository.validateProductEdit(product)
        }.onFailure {
            Analytics.logNonFatalException(it)
        }
    }
}

