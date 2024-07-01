package org.codingforanimals.veganuniverse.create.product.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.commons.network.PermissionDeniedException
import org.codingforanimals.veganuniverse.create.product.domain.model.ProductForm
import org.codingforanimals.veganuniverse.commons.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductQueryParams
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

private const val TAG = "SubmitProduct"

class SubmitProduct(
    private val productRepository: ProductRepository,
    private val profileContentUseCases: ProfileContentUseCases,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    suspend operator fun invoke(productForm: ProductForm): Result {
            val user = flowOnCurrentUser(true).firstOrNull() ?: return Result.GuestUser
            if (!user.isEmailVerified) {
                return Result.UnverifiedEmail
            }

        return try {
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
        } catch (e: PermissionDeniedException) {
            Result.UserMustReauthenticate
        } catch (e: Throwable) {
            Log.e(TAG, e.stackTraceToString())
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
        data object GuestUser : Result()
        data object UnexpectedError : Result()
        data object UnverifiedEmail : Result()
        data object UserMustReauthenticate : Result()
        data class AlreadyExists(val productId: String) : Result()
        data class Success(val productId: String) : Result()
    }
}