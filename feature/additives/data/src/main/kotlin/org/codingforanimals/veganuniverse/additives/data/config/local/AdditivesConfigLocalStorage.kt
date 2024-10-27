package org.codingforanimals.veganuniverse.additives.data.config.local

import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.additives.data.config.local.model.AdditivesConfigLocalModel

interface AdditivesConfigLocalStorage {
    suspend fun setConfig(value: AdditivesConfigLocalModel)
    fun flowOnConfig(): Flow<AdditivesConfigLocalModel?>
}
