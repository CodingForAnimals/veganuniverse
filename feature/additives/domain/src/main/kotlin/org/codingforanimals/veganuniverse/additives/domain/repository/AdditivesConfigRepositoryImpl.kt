package org.codingforanimals.veganuniverse.additives.domain.repository

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withTimeout
import org.codingforanimals.veganuniverse.additives.data.config.local.AdditivesConfigLocalStorage
import org.codingforanimals.veganuniverse.additives.data.config.remote.AdditivesConfigRemoteSource
import org.codingforanimals.veganuniverse.additives.domain.mapper.toDto
import org.codingforanimals.veganuniverse.additives.domain.mapper.toDomain
import org.codingforanimals.veganuniverse.additives.domain.model.AdditivesConfig

internal class AdditivesConfigRepositoryImpl(
    private val remoteDataSource: AdditivesConfigRemoteSource,
    private val localStorage: AdditivesConfigLocalStorage,
) : AdditivesConfigRepository {
    override suspend fun getConfigFromRemote(): AdditivesConfig {
        return withTimeout(REMOTE_CONFIG_MAX_TIMEOUT) {
            val configDTO = checkNotNull(remoteDataSource.getAdditivesConfig()) {
                "Error getting additives config from remote data source"
            }
            configDTO.toDomain()
        }
    }

    override suspend fun getConfigFromLocal(): AdditivesConfig? {
        return localStorage.flowOnConfig().firstOrNull()?.toDomain()
    }

    override suspend fun setLocalConfig(config: AdditivesConfig) {
        return localStorage.setConfig(config.toDto())
    }

    companion object {
        private const val REMOTE_CONFIG_MAX_TIMEOUT = 5_000L
    }
}
