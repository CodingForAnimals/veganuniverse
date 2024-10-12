package org.codingforanimals.veganuniverse.additives.domain.repository

import org.codingforanimals.veganuniverse.additives.domain.model.AdditivesConfig

interface AdditivesConfigRepository {
    suspend fun getConfigFromRemote(): AdditivesConfig
    suspend fun getConfigFromLocal(): AdditivesConfig?
    suspend fun setLocalConfig(config: AdditivesConfig)
}
