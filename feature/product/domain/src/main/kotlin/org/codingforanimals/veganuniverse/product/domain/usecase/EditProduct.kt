package org.codingforanimals.veganuniverse.product.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class EditProduct(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(productId: String, suggestion: String): Result {
        val user = flowOnCurrentUser(true).firstOrNull() ?: return Result.UnauthenticatedUser
        if (!user.isEmailVerified) {
            return Result.UnverifiedEmail
        }
        return runCatching {
            productRepository.editProduct(productId, user.id, suggestion)
            Result.Success
        }.getOrElse {
            Log.e(TAG, it.stackTraceToString())
            Result.UnexpectedError
        }
    }

    companion object {
        private const val TAG = "EditProduct"
    }

    sealed class Result {
        data object UnauthenticatedUser : Result()
        data object UnexpectedError : Result()
        data object UnverifiedEmail : Result()
        data object Success : Result()
    }
}
