package org.codingforanimals.veganuniverse.product.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.codingforanimals.veganuniverse.product.data.source.GetLatestProductsRemoteDataSource
import org.codingforanimals.veganuniverse.product.domain.mapper.toDomainModel
import org.codingforanimals.veganuniverse.product.domain.model.Product

class GetLatestProducts(
    private val remoteDataSource: GetLatestProductsRemoteDataSource,
) {
    operator fun invoke(): Flow<State> = flow {
        cache?.let { inMemoryProducts ->
            emit(State.Success(inMemoryProducts))
        } ?: let {
            emit(State.Loading)
            runCatching {
                remoteDataSource.getProducts()
            }.getOrNull()?.let { remoteProducts ->
                val products = remoteProducts.map { it.toDomainModel() }
                cache = products
                emit(State.Success(products))
            } ?: emit(State.Error)
        }
    }

    sealed class State {
        data object Loading : State()
        data object Error : State()
        data class Success(val products: List<Product>) : State()
    }

    companion object {
        private var cache: List<Product>? = null
    }
}
