package org.codingforanimals.veganuniverse.additives.data.config.remote

import org.codingforanimals.veganuniverse.additives.data.config.model.AdditivesConfigDTO

interface AdditivesConfigRemoteSource {
    suspend fun getAdditivesConfig(): AdditivesConfigDTO?
}

