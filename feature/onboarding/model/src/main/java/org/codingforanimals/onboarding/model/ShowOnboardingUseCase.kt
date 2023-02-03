package org.codingforanimals.onboarding.model

import kotlinx.coroutines.withContext
import org.codingforanimals.onboarding.data.OnboardingDataStoreManager
import org.codingforanimals.veganuniverse.common.dispatcher.CoroutineDispatcherProvider

class ShowOnboardingUseCase(
    private val onboardingDataStoreManager: OnboardingDataStoreManager,
    private val dispatcherProvider: CoroutineDispatcherProvider,
) {
    suspend operator fun invoke(): Boolean =
        withContext(dispatcherProvider.main()) { !onboardingDataStoreManager.wasOnboardingDismissed() }
}