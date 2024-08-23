package org.codingforanimals.veganuniverse.product.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class EditProduct(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(productId: String, suggestion: String): Result<Unit> = runCatching {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to edit a product"
        }
        productRepository.editProduct(productId, user.id, suggestion)
    }.onFailure {
        Log.e("EditProduct", "Error editing product", it)
        Analytics.logNonFatalException(it)
    }
}
