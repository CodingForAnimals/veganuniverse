package org.codingforanimals.veganuniverse.product.domain.usecase

import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.product.domain.model.ProductEdit
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository

class GetProductEdits(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(): List<ProductEdit> {
        return try {
            productRepository.getProductEditsFromRemote()
        } catch (e: Throwable) {
            Analytics.logNonFatalException(e)
            throw e
        }
    }
}
