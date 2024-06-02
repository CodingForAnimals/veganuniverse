package org.codingforanimals.veganuniverse.product.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.codingforanimals.veganuniverse.product.data.source.GetPaginatedProductsRemoteDataSource
import org.codingforanimals.veganuniverse.product.domain.ProductRepository
import org.codingforanimals.veganuniverse.product.domain.mapper.toDomainModel
import org.codingforanimals.veganuniverse.product.domain.model.Product

class GetPaginatedProducts(
    private val remoteDataSource: GetPaginatedProductsRemoteDataSource,
) {
    operator fun invoke(name: String, category: String?, type: String?): Flow<PagingData<Product>> {
        return remoteDataSource.getProducts(name, category, type)
            .map { pagingData -> pagingData.map { productEntity -> productEntity.toDomainModel() } }
    }
}

class ProductUseCases(
    private val repository: ProductRepository,
) {
}
