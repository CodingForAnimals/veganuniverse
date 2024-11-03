package org.codingforanimals.veganuniverse.product.domain.usecase

import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.product.domain.model.ProductEdit
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository

class DeleteProductEdit(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(edit: ProductEdit): Result<Unit> {
        return runCatching {
            productRepository.deleteProductEdit(edit)
        }.onFailure {
            Analytics.logNonFatalException(it)
        }
    }
}

