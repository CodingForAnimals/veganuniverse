package org.codingforanimals.veganuniverse.additives.domain.usecase

import org.codingforanimals.veganuniverse.additives.domain.model.AdditivesConfig
import org.codingforanimals.veganuniverse.additives.domain.repository.AdditiveRepository
import org.codingforanimals.veganuniverse.additives.domain.repository.AdditivesConfigRepository
import org.codingforanimals.veganuniverse.commons.analytics.Analytics

class CheckForAdditivesUpdate(
    private val additiveRepository: AdditiveRepository,
    private val configRepository: AdditivesConfigRepository
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        val localConfig = configRepository.getConfigFromLocal()
        val remoteConfig = configRepository.getConfigFromRemote()

        if (requiresUpdate(localConfig, remoteConfig)) {
            val remote = additiveRepository.getAdditivesFromRemote()

            additiveRepository.clearLocalAdditives()
            additiveRepository.setLocalAdditives(remote)

            configRepository.setLocalConfig(remoteConfig)
        }
    }.onFailure { throwable ->
        Analytics.logNonFatalException(throwable)
    }

    private fun requiresUpdate(
        localConfig: AdditivesConfig?,
        remoteConfig: AdditivesConfig
    ): Boolean {
        return (localConfig?.version ?: 0) < remoteConfig.version
    }
}
