package org.codingforanimals.veganuniverse.product.domain.repository

import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.product.data.config.local.ProductConfigLocalSource
import org.codingforanimals.veganuniverse.product.data.config.remote.ProductConfigRemoteSource
import org.codingforanimals.veganuniverse.product.domain.model.ProductConfig
import org.codingforanimals.veganuniverse.product.domain.model.toDomain
import org.codingforanimals.veganuniverse.product.domain.model.toLocal

internal class ProductConfigRepositoryImpl(
    private val localDataSource: ProductConfigLocalSource,
    private val remoteDataSource: ProductConfigRemoteSource,
) : ProductConfigRepository {
    override suspend fun getConfigFromLocal(): ProductConfig {
        return (localDataSource.flowOnConfig().firstOrNull()?.toDomain() ?: DEFAULT_CONFIG)
    }

    override suspend fun saveConfigToLocal(config: ProductConfig) {
        localDataSource.setConfig(config.toLocal())
    }

    override suspend fun getConfigFromRemote(): ProductConfig {
        return remoteDataSource.getProductConfig().toDomain()
    }

    companion object {
        private val DEFAULT_CONFIG = ProductConfig(
            version = 0,
        )
    }
}
