package org.codingforanimals.veganuniverse.onboarding.data

import kotlinx.coroutines.flow.Flow

interface OnboardingLocalStorage {
    suspend fun wasOnboardingDismissed(): Flow<Boolean>
    suspend fun setOnboardingAsDismissed()
}
