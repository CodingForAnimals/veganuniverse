package org.codingforanimals.veganuniverse.create.product.domain.usecase

import android.util.Log
import org.codingforanimals.veganuniverse.create.product.domain.model.ProductForm
import org.codingforanimals.veganuniverse.network.NetworkUtils
import org.codingforanimals.veganuniverse.product.domain.ProductRepository
import org.codingforanimals.veganuniverse.product.model.Product
import org.codingforanimals.veganuniverse.product.model.ProductQueryParams
import org.codingforanimals.veganuniverse.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.user.domain.repository.CurrentUserRepository

private const val TAG = "SubmitProduct"

class SubmitProduct(
    private val currentUserRepository: CurrentUserRepository,
    private val productRepository: ProductRepository,
    private val profileContentUseCases: ProfileContentUseCases,
    private val networkUtils: NetworkUtils,
) {
    suspend operator fun invoke(productForm: ProductForm): Result {
        return runCatching {
            if (!networkUtils.isNetworkAvailable()) return Result.NoInternet
            var user = currentUserRepository.getCurrentUser() ?: return Result.GuestUser
            if (!user.isEmailVerified) {
                val refreshedUser = currentUserRepository.refreshUser() ?: return Result.GuestUser
                refreshedUser.takeIf { it.isEmailVerified }?.let { user = it }
                    ?: Result.UnverifiedEmail
            }

            val existingProductId = productRepository.queryProducts(
                params = ProductQueryParams.Builder()
                    .withMaxSize(1)
                    .withPageSize(1)
                    .validated(true)
                    .withExactSearch(
                        name = productForm.name,
                        brand = productForm.brand,
                    )
                    .build()
            ).firstOrNull()?.id

            if (existingProductId != null) {
                return Result.AlreadyExists(existingProductId)
            }

            val productFormAsModel = productForm.toModel(user.id, user.name)
            val newId = productRepository.insertProduct(productFormAsModel, productForm.imageModel)
            profileContentUseCases.addContribution(newId)
            Result.Success(newId)
        }.getOrElse {
            Log.e(TAG, it.stackTraceToString())
            Result.UnexpectedError
        }
    }

    private fun ProductForm.toModel(userId: String, username: String): Product {
        return Product(
            id = null,
            userId = userId,
            username = username,
            name = name.trim(),
            brand = brand.trim(),
            comment = comment?.trim(),
            type = type,
            category = category,
            createdAt = null,
            imageUrl = null,
            validated = false,
        )
    }

    sealed class Result {
        data object NoInternet : Result()
        data object GuestUser : Result()
        data object UnexpectedError : Result()
        data object UnverifiedEmail : Result()
        data class AlreadyExists(val productId: String) : Result()
        data class Success(val productId: String) : Result()
    }
}