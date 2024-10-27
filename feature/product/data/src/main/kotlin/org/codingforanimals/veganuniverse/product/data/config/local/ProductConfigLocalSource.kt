package org.codingforanimals.veganuniverse.product.data.config.local

import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.product.data.config.local.model.ProductConfigLocalModel

interface ProductConfigLocalSource {
    fun flowOnConfig(): Flow<ProductConfigLocalModel?>
    suspend fun setConfig(config: ProductConfigLocalModel)
}

