package org.codingforanimals.veganuniverse.product.domain.usecase

import android.os.Parcelable
import android.util.Log
import kotlinx.coroutines.flow.first
import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.ProfileContentUseCases
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.FlowOnCurrentUser
import org.codingforanimals.veganuniverse.product.domain.model.Product
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository

class UploadProduct(
    private val flowOnCurrentUser: FlowOnCurrentUser,
    private val productRepository: ProductRepository,
    private val profileProductUseCases: ProfileContentUseCases
) {
    suspend operator fun invoke(product: Product, imageModel: Parcelable?): Result<String> = runCatching {
        val user = checkNotNull(flowOnCurrentUser().first()) {
            "User must be logged in to submit a product"
        }

        if (productRepository.getByBrandAndName(product.brand, product.name) != null) {
            return Result.failure(ProductConflictException("A product with the same name and brand already exists"))
        }


        val productWithUserInfo = product.copy(
            userId = user.id,
            username = user.name,
        )
        productRepository.uploadUnvalidatedProductToRemote(productWithUserInfo, imageModel).also {
            profileProductUseCases.addContribution(it)
        }
    }.onFailure {
        Log.e("UploadProduct", "Error uploading product", it)
        Analytics.logNonFatalException(it)
    }

    class ProductConflictException(message: String) : Exception(message)
}
