package org.codingforanimals.veganuniverse.additives.data.config.remote

import org.codingforanimals.veganuniverse.additives.data.config.remote.model.AdditivesConfigDTO

interface AdditivesConfigRemoteSource {
    suspend fun getAdditivesConfig(): AdditivesConfigDTO?
}

