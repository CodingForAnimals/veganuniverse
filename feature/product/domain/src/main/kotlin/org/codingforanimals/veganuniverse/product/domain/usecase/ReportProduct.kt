package org.codingforanimals.veganuniverse.product.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class ReportProduct(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(productId: String): Result {
        val user = flowOnCurrentUser(true).firstOrNull() ?: return Result.UnauthenticatedUser
        if (!user.isVerified) {
            return Result.UnverifiedEmail
        }
        return runCatching {
            productRepository.reportProduct(productId, user.id)
            Result.Success
        }.getOrElse {
            Log.e(TAG, it.stackTraceToString())
            Result.UnexpectedError
        }
    }

    sealed class Result {
        data object UnauthenticatedUser : Result()
        data object UnexpectedError : Result()
        data object UnverifiedEmail : Result()
        data object Success : Result()
    }

    companion object {
        private const val TAG = "ReportProduct"
    }
}
