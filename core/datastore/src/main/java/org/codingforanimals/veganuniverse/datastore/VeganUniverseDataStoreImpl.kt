package org.codingforanimals.veganuniverse.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class VeganUniverseDataStoreImpl(
    private val dataStore: DataStore<Preferences>,
) : VeganUniverseDataStore {

    private val tag = VeganUniverseDataStoreImpl::class.simpleName

    override suspend fun saveValue(key: Preferences.Key<Boolean>, value: Boolean): Boolean {
        return try {
            dataStore.edit { it[key] = value }
            true
        } catch (e: Throwable) {
            Log.e(tag, e.stackTraceToString())
            false
        }
    }

    override suspend fun readValue(key: Preferences.Key<Boolean>): Boolean? =
        dataStore.data.map { it[key] }.firstOrNull()
}