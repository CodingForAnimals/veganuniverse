package org.codingforanimals.veganuniverse.onboarding.presentation.di

import org.codingforanimals.veganuniverse.common.dispatcher.coroutineDispatcherModule
import org.codingforanimals.veganuniverse.datastore.di.dataStoreModule
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingDataStoreManager
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingDataStoreManagerImpl
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingViewModel
import org.codingforanimals.veganuniverse.onboarding.presentation.SetOnboardingAsDismissedUseCase
import org.codingforanimals.veganuniverse.onboarding.presentation.ShowOnboardingUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val onboardingModule = module {
    includes(dataStoreModule, coroutineDispatcherModule)
    factory<OnboardingDataStoreManager> { OnboardingDataStoreManagerImpl(get()) }
    factory { ShowOnboardingUseCase(get(), get()) }
    factory { SetOnboardingAsDismissedUseCase(get(), get()) }
    viewModel { OnboardingViewModel(get()) }
}