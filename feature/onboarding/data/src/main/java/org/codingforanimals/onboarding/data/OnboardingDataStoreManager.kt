package org.codingforanimals.onboarding.data

interface OnboardingDataStoreManager {
    suspend fun wasOnboardingDismissed(): Boolean
    suspend fun setOnboardingAsDismissed()

    companion object Keys {
        const val OnboardingDismissedKey = "IS_ONBOARDING_DISMISSED"
    }
}