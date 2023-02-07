package org.codingforanimals.veganuniverse.onboarding.data

import org.codingforanimals.veganuniverse.datastore.VeganUniverseDataStore

class OnboardingDataStoreManagerImpl(
    private val dataStore: VeganUniverseDataStore,
) : OnboardingDataStoreManager {

    override suspend fun setOnboardingAsDismissed() {
        dataStore.saveValue(OnboardingDataStoreManager.OnboardingDismissedKey, true)
    }

    override suspend fun wasOnboardingDismissed(): Boolean =
        dataStore.readValue(OnboardingDataStoreManager.OnboardingDismissedKey) ?: false

}