package org.codingforanimals.veganuniverse.create.product.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.create.product.domain.model.ProductForm
import org.codingforanimals.veganuniverse.commons.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductQueryParams
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser

class SubmitProduct(
    private val productRepository: ProductRepository,
    private val profileProductUseCases: ProfileContentUseCases,
    private val flowOnCurrentUser: FlowOnCurrentUser,
) {
    suspend operator fun invoke(productForm: ProductForm): Result<String> = runCatching {
        val user = checkNotNull(flowOnCurrentUser().first()){
            "User must be logged in to submit a product"
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
            return Result.failure(ProductConflictException("A product with the same name and brand already exists"))
        }

        val productFormAsModel = productForm.toModel(user.id, user.name)
        productRepository.insertProduct(productFormAsModel, productForm.imageModel).also {
            profileProductUseCases.addContribution(it)
        }
    }.onFailure {
        Log.e("SubmitProduct", "Error submitting product", it)
        Analytics.logNonFatalException(it)
    }

    private fun ProductForm.toModel(userId: String, username: String): Product {
        return Product(
            id = null,
            userId = userId,
            username = username,
            name = name.trim(),
            brand = brand?.trim(),
            comment = comment?.trim(),
            type = type,
            category = category,
            createdAt = null,
            imageUrl = null,
            validated = false,
        )
    }

    class ProductConflictException(message: String) : Exception(message)
}