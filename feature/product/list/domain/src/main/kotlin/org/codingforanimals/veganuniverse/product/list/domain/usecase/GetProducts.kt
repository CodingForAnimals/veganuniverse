package org.codingforanimals.veganuniverse.product.list.domain.usecase

import androidx.paging.map
import kotlinx.coroutines.flow.map
import org.codingforanimals.veganuniverse.product.list.data.source.GetProductsRemoteDataSource
import org.codingforanimals.veganuniverse.product.list.domain.mapper.toDomainModel

class GetProducts(
    private val remoteDataSource: GetProductsRemoteDataSource,
) {
    operator fun invoke(name: String, category: String?, type: String?) =
        remoteDataSource.getProducts(name, category, type)
            .map { pagingData -> pagingData.map { productEntity -> productEntity.toDomainModel() } }
}
