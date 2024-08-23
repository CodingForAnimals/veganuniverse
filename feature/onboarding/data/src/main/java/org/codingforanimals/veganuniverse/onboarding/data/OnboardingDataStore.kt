package org.codingforanimals.veganuniverse.onboarding.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal val Context.onboardingDataStore by preferencesDataStore("vegan_universe_data_store")

internal class OnboardingDataStore(
    private val dataStore: DataStore<Preferences>,
) : OnboardingLocalStorage {

    private val userHasSeenOnboardingKey = booleanPreferencesKey(USER_HAS_SEEN_ONBOARDING)

    override suspend fun setOnboardingAsDismissed() {
        dataStore.edit { preferences ->
            preferences[userHasSeenOnboardingKey] = true
        }
    }

    override suspend fun wasOnboardingDismissed(): Flow<Boolean> {
        return dataStore.data.map { it[userHasSeenOnboardingKey] ?: false }
    }

    companion object {
        private const val USER_HAS_SEEN_ONBOARDING = "user_has_seen_onboarding"
    }
}