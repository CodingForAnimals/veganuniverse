package org.codingforanimals.veganuniverse.onboarding.domain

import kotlinx.coroutines.flow.Flow
import org.codingforanimals.veganuniverse.onboarding.data.OnboardingLocalStorage

internal class OnboardingRepositoryImpl(
    private val onboardingLocalStorage: OnboardingLocalStorage,
) : OnboardingRepository {
    override fun wasOnboardingDismissed(): Flow<Boolean> {
        return onboardingLocalStorage.wasOnboardingDismissed()
    }

    override suspend fun setWasOnboardingDismissed(value: Boolean) {
        onboardingLocalStorage.setWasOnboardingDismissed(value)
    }
}
