package org.codingforanimals.veganuniverse.additives.data.config.local

import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.additives.data.config.model.AdditivesConfigDTO

interface AdditivesConfigLocalStorage {
    suspend fun setConfig(value: AdditivesConfigDTO)
    fun flowOnConfig(): Flow<AdditivesConfigDTO?>
}
