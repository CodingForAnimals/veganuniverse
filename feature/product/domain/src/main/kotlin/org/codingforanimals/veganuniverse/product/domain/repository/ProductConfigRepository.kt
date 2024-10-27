package org.codingforanimals.veganuniverse.product.domain.repository

import org.codingforanimals.veganuniverse.product.domain.model.ProductConfig

interface ProductConfigRepository {
    suspend fun getConfigFromLocal(): ProductConfig
    suspend fun saveConfigToLocal(config: ProductConfig)
    suspend fun getConfigFromRemote(): ProductConfig
}
