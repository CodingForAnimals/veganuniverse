package org.codingforanimals.veganuniverse.product.data.config.remote

import org.codingforanimals.veganuniverse.product.data.config.remote.model.ProductConfigDTO

interface ProductConfigRemoteSource {
    suspend fun getProductConfig(): ProductConfigDTO
}
