package org.codingforanimals.veganuniverse.product.domain.model

import org.codingforanimals.veganuniverse.product.data.config.local.model.ProductConfigLocalModel
import org.codingforanimals.veganuniverse.product.data.config.remote.model.ProductConfigDTO

internal fun ProductConfigDTO.toDomain(): ProductConfig {
    val version = checkNotNull(version) {
        "ProductConfigDTO version cannot be null"
    }
    return ProductConfig(
        version = version,
    )
}

internal fun ProductConfigLocalModel.toDomain(): ProductConfig {
    return ProductConfig(
        version = version,
    )
}

internal fun ProductConfig.toLocal(): ProductConfigLocalModel {
    return ProductConfigLocalModel(
        version = version,
    )
}
