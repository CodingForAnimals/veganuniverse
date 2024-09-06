package org.codingforanimals.veganuniverse.validator.product.domain

import android.util.Log
import org.codingforanimals.veganuniverse.commons.product.domain.repository.ProductRepository

internal class ValidateProductUseCase(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return runCatching {
            productRepository.validateProduct(id)
        }.onFailure {
            Log.e(TAG, "Error validating product", it)
        }
    }

    companion object {
        private const val TAG = "ValidateProductUseCase"
    }
}
