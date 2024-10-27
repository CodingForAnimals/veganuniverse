package org.codingforanimals.veganuniverse.product.domain.usecase

import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository

class GetProductDetail(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(id: String, unvalidated: Boolean = false): Product? {
        return try {
            if (unvalidated) {
                repository.getUnvalidatedProductByIdFromRemote(id)
            } else {
                repository.getProductByIdFromLocal(id)
            }
        } catch (e: Throwable) {
            Analytics.logNonFatalException(e)
            null
        }
    }
}