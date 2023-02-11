package org.codingforanimals.veganuniverse.onboarding.model

import kotlinx.coroutines.withContext
import org.codingforanimals.veganuniverse.onboarding.data.OnboardingDataStoreManager
import org.codingforanimals.veganuniverse.coroutines.CoroutineDispatcherProvider

class ShowOnboardingUseCase(
    private val onboardingDataStoreManager: OnboardingDataStoreManager,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) {
    suspend operator fun invoke(): Boolean =
        withContext(dispatcherProvider.main()) { !onboardingDataStoreManager.wasOnboardingDismissed() }
}