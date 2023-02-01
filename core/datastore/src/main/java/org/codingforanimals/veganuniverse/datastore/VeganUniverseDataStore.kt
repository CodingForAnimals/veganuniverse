package org.codingforanimals.veganuniverse.datastore

interface VeganUniverseDataStore {
    suspend fun saveValue(key: String, value: Boolean): Boolean
    suspend fun readValue(key: String): Boolean?
}