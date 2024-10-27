package org.codingforanimals.veganuniverse.additives.data.config.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.codingforanimals.veganuniverse.additives.data.config.local.AdditivesConfigDataStore.Companion.DATASTORE_NAME
import org.codingforanimals.veganuniverse.additives.data.config.local.model.AdditivesConfigLocalModel

internal val Context.additivesConfigDataStore: DataStore<Preferences> by preferencesDataStore(
    DATASTORE_NAME
)

internal class AdditivesConfigDataStore(
    private val dataStore: DataStore<Preferences>,
) : AdditivesConfigLocalStorage {
    private val versionKey = intPreferencesKey(VERSION_KEY)

    companion object {
        private const val VERSION_KEY = "version"
        internal const val DATASTORE_NAME = "additives-datastore"
    }

    override suspend fun setConfig(value: AdditivesConfigLocalModel) {
        dataStore.edit { preferences ->
            preferences[versionKey] = value.version
        }
    }

    override fun flowOnConfig(): Flow<AdditivesConfigLocalModel?> {
        return dataStore.data.map { preferences ->
            preferences[versionKey] ?: 0
        }.distinctUntilChanged().map {
            AdditivesConfigLocalModel(it)
        }
    }
}