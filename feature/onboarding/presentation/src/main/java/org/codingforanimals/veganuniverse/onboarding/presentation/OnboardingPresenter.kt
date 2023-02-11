package org.codingforanimals.veganuniverse.onboarding.presentation

import org.codingforanimals.veganuniverse.onboarding.model.SetOnboardingAsDismissedUseCase
import org.codingforanimals.veganuniverse.onboarding.model.ShowOnboardingUseCase

internal class OnboardingPresenterImpl(
    private val showOnboardingUseCase: ShowOnboardingUseCase,
    private val setOnboardingAsDismissedUseCase: SetOnboardingAsDismissedUseCase,
): OnboardingPresenter {
    override suspend fun showOnboarding() = showOnboardingUseCase()
    override suspend fun setOnboardingAsDismissed() = setOnboardingAsDismissedUseCase()
}

interface OnboardingPresenter {
    suspend fun showOnboarding(): Boolean
    suspend fun setOnboardingAsDismissed()
}