package org.codingforanimals.veganuniverse.onboarding.model

import org.codingforanimals.veganuniverse.onboarding.data.OnboardingLocalStorage

class ShowOnboardingUseCase(
    private val onboardingLocalStorage: OnboardingLocalStorage,
) {
    suspend operator fun invoke(): Boolean {
        return false
//        return withContext(dispatcherProvider.main()) { !onboardingDataStoreManager.wasOnboardingDismissed() }
    }
}