package org.codingforanimals.veganuniverse.onboarding.presentation

import org.codingforanimals.onboarding.model.SetOnboardingAsDismissedUseCase
import org.codingforanimals.onboarding.model.ShowOnboardingUseCase

class OnboardingPresenter(
    private val showOnboardingUseCase: ShowOnboardingUseCase,
    private val setOnboardingAsDismissedUseCase: SetOnboardingAsDismissedUseCase,
) {
    suspend fun showOnboarding() = showOnboardingUseCase()
    suspend fun setOnboardingAsDismissed() = setOnboardingAsDismissedUseCase()
}