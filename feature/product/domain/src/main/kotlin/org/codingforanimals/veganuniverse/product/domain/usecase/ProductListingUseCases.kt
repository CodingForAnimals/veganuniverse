package org.codingforanimals.veganuniverse.product.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.profile.domain.usecase.GetProfile

class ProductListingUseCases(
    private val getProfile: GetProfile,
    private val productRepository: ProductRepository,
) {
    suspend fun getBookmarksIds(): List<String> {
        return getProfile()?.bookmarks?.products.orEmpty()
    }

    suspend fun getContributionsIds(): List<String> {
        return getProfile()?.contributions?.products.orEmpty()
    }

    fun queryProductsByIds(ids: List<String>): Flow<PagingData<Product>> {
        return productRepository.queryProductsById(ids)
    }
}
