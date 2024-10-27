package org.codingforanimals.veganuniverse.product.data.config.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.codingforanimals.veganuniverse.product.data.config.local.model.ProductConfigLocalModel

internal val Context.productConfigDataStore: DataStore<Preferences> by preferencesDataStore(
    ProductConfigDataStore.DATA_STORE_NAME
)

internal class ProductConfigDataStore(
    private val dataStore: DataStore<Preferences>,
) : ProductConfigLocalSource {

    private val versionKey = intPreferencesKey(VERSION_KEY)

    override fun flowOnConfig(): Flow<ProductConfigLocalModel?> {
        return dataStore.data.map { preferences ->
            preferences[versionKey]?.let { version ->
                ProductConfigLocalModel(
                    version = version,
                )
            }
        }
    }

    override suspend fun setConfig(config: ProductConfigLocalModel) {
        dataStore.edit { preferences ->
            preferences[versionKey] = config.version
        }
    }

    companion object {
        private const val VERSION_KEY = "version"
        internal const val DATA_STORE_NAME = "product_config"
    }
}
