package org.codingforanimals.veganuniverse.onboarding.model

import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.onboarding.data.OnboardingDataStoreManager

class SetOnboardingAsDismissedUseCase(
    private val onboardingDataStoreManager: OnboardingDataStoreManager,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) {
    suspend operator fun invoke() =
        withContext(dispatcherProvider.io()) {
            onboardingDataStoreManager.setOnboardingAsDismissed() }
}