package org.codingforanimals.veganuniverse.user.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.firstOrNull
import org.codingforanimals.veganuniverse.user.data.dto.UserDTO

internal val Context.userDataStore by preferencesDataStore("user-data-store")

internal class UserDataStore(
    private val dataStore: DataStore<Preferences>,
) : UserLocalStorage {

    private val idKey = stringPreferencesKey(USER_ID)
    private val emailKey = stringPreferencesKey(EMAIL_ID)
    private val nameKey = stringPreferencesKey(NAME_ID)

    override suspend fun getCurrentUser(): UserDTO? {
        return dataStore.data.firstOrNull()?.let { preferences ->
            val id = preferences[idKey] ?: return null
            val email = preferences[emailKey] ?: return null
            val name = preferences[nameKey] ?: return null
            val isEmailVerified =
                FirebaseAuth.getInstance().currentUser?.isEmailVerified ?: return null
            UserDTO(
                userId = id,
                email = email,
                name = name,
                isEmailVerified = isEmailVerified,
            )
        }
    }

    override suspend fun setCurrentUser(userDTO: UserDTO) {
        dataStore.edit { preferences ->
            preferences[idKey] = userDTO.userId
            preferences[emailKey] = userDTO.email
            preferences[nameKey] = userDTO.name
        }
    }

    override suspend fun clearCurrentUser() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private const val USER_ID = "user-id"
        private const val EMAIL_ID = "email-id"
        private const val NAME_ID = "name-id"
    }
}
