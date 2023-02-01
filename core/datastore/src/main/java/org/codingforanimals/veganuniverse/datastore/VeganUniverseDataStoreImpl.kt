package org.codingforanimals.veganuniverse.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class VeganUniverseDataStoreImpl(
    private val dataStore: DataStore<Preferences>,
) : VeganUniverseDataStore {

    private val tag = VeganUniverseDataStoreImpl::class.simpleName

    override suspend fun saveValue(key: String, value: Boolean): Boolean {
        dataStore.edit { it[booleanPreferencesKey(key)] = false }
        return try {
            dataStore.edit { it[booleanPreferencesKey(key)] = value }
            true
        } catch (e: Throwable) {
            Log.e(tag, e.stackTraceToString())
            false
        }
    }

    override suspend fun readValue(key: String): Boolean? =
        dataStore.data.map { it[booleanPreferencesKey(key)] }.firstOrNull()
}