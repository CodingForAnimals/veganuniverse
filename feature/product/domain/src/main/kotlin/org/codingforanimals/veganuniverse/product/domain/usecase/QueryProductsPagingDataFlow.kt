package org.codingforanimals.veganuniverse.product.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.commons.product.domain.repository.ProductRepository
import org.codingforanimals.veganuniverse.commons.product.shared.model.Product
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductQueryParams

class QueryProductsPagingDataFlow(
    private val productRepository: ProductRepository,
) {
    operator fun invoke(params: ProductQueryParams): Flow<PagingData<Product>> {
        return productRepository.queryProductsPagingDataFlow(params)
    }
}
