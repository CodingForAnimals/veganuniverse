package org.codingforanimals.veganuniverse.product.domain.usecase

import org.codingforanimals.veganuniverse.commons.analytics.Analytics
import org.codingforanimals.veganuniverse.product.domain.model.ProductConfig
import org.codingforanimals.veganuniverse.product.domain.repository.ProductConfigRepository
import org.codingforanimals.veganuniverse.product.domain.repository.ProductRepository

class CheckForProductsUpdate(
    private val configRepository: ProductConfigRepository,
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        val localConfig = configRepository.getConfigFromLocal()
        val remoteConfig = configRepository.getConfigFromRemote()

        if (requiresUpdate(localConfig, remoteConfig)) {
            productRepository.updateProductsFromRemoteToLocal()
            configRepository.saveConfigToLocal(remoteConfig)
        }
    }.onFailure { throwable ->
        Analytics.logNonFatalException(throwable)
    }

    private fun requiresUpdate(localConfig: ProductConfig?, remoteConfig: ProductConfig): Boolean {
        return (localConfig?.version ?: 0) < remoteConfig.version
    }
}