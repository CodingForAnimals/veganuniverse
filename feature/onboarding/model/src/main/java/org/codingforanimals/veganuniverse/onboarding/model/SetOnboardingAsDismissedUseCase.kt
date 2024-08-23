package org.codingforanimals.veganuniverse.onboarding.model

import org.codingforanimals.veganuniverse.onboarding.data.OnboardingLocalStorage

class SetOnboardingAsDismissedUseCase(
    private val onboardingLocalStorage: OnboardingLocalStorage,
) {
    suspend operator fun invoke() = onboardingLocalStorage.setOnboardingAsDismissed()
}