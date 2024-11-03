package org.codingforanimals.veganuniverse.product.domain.usecase

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.model.ProductEdit
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository

class ValidateAllProducts(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(
        products: List<Product>,
        edits: List<ProductEdit>,
    ): Result<Unit> {
        return runCatching {
            coroutineScope {
                products.forEach {
                    launch { productRepository.validateProduct(it) }
                }
                edits.forEach {
                    launch { productRepository.validateProductEdit(it) }
                }
            }
        }.onFailure {
            Analytics.logNonFatalException(it)
        }
    }
}
