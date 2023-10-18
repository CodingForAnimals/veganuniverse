package org.codingforanimals.veganuniverse.onboarding.model

import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.onboarding.data.OnboardingDataStoreManager

class ShowOnboardingUseCase(
    private val onboardingDataStoreManager: OnboardingDataStoreManager,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) {
    suspend operator fun invoke(): Boolean {
        return false
//        return withContext(dispatcherProvider.main()) { !onboardingDataStoreManager.wasOnboardingDismissed() }
    }
}