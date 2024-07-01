package org.codingforanimals.veganuniverse.commons.user.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.codingforanimals.veganuniverse.commons.user.data.dto.UserDTO

internal val Context.userDataStore by preferencesDataStore("user-data-store")

internal class UserDataStore(
    private val dataStore: DataStore<Preferences>,
) : UserLocalStorage {

    private val idKey = stringPreferencesKey(ID)
    private val emailKey = stringPreferencesKey(EMAIL)
    private val nameKey = stringPreferencesKey(NAME)
    private val isEmailVerifiedKey = booleanPreferencesKey(IS_EMAIL_VERIFIED)

    override fun flowOnCurrentUser(): Flow<UserDTO?> {
        return dataStore.data.transform { preferences ->
            val id = preferences[idKey] ?: return@transform emit(null)
            val email = preferences[emailKey] ?: return@transform emit(null)
            val name = preferences[nameKey] ?: return@transform emit(null)
            val isEmailVerified = preferences[isEmailVerifiedKey] ?: false
            emit(
                UserDTO(
                    userId = id,
                    email = email,
                    name = name,
                    isEmailVerified = isEmailVerified,
                )
            )
        }
    }

    override suspend fun setCurrentUser(userDTO: UserDTO) {
        dataStore.edit { preferences ->
            preferences[idKey] = userDTO.userId
            preferences[emailKey] = userDTO.email
            preferences[nameKey] = userDTO.name
            preferences[isEmailVerifiedKey] = userDTO.isEmailVerified
        }
    }

    override suspend fun clearCurrentUser() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private const val ID = "id"
        private const val EMAIL = "email"
        private const val NAME = "name"
        private const val IS_EMAIL_VERIFIED = "is_email_verified"
    }
}
