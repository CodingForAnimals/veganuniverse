package org.codingforanimals.veganuniverse.product.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class GetProductDetail(
    private val productRepository: ProductRepository,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    suspend operator fun invoke(id: String): Result? {
        return runCatching {
            productRepository.getProductById(id)?.let {
                val userId = flowOnCurrentUser().firstOrNull()?.id
                Result(
                    product = it,
                    isOwned = it.userId == userId,
                )
            }
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
            Analytics.logNonFatalException(it)
        }.getOrNull()
    }

    data class Result(
        val product: Product,
        val isOwned: Boolean,
    )

    companion object {
        private const val TAG = "GetProduct"
    }
}
