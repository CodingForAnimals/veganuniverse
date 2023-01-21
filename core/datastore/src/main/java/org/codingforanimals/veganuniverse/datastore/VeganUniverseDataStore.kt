package org.codingforanimals.veganuniverse.datastore

import androidx.datastore.preferences.core.Preferences

interface VeganUniverseDataStore {
    suspend fun saveValue(key: Preferences.Key<Boolean>, value: Boolean): Boolean
    suspend fun readValue(key: Preferences.Key<Boolean>): Boolean?
}