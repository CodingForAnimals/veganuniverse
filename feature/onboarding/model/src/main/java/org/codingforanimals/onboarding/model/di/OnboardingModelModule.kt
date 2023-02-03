package org.codingforanimals.onboarding.model.di

import org.codingforanimals.onboarding.data.di.onboardingDataModule
import org.codingforanimals.onboarding.model.SetOnboardingAsDismissedUseCase
import org.codingforanimals.onboarding.model.ShowOnboardingUseCase
import org.codingforanimals.veganuniverse.common.dispatcher.coroutineDispatcherModule
import org.koin.dsl.module

val onboardingModelModule = module {
    includes(onboardingDataModule, coroutineDispatcherModule)
    factory { ShowOnboardingUseCase(get(), get()) }
    factory { SetOnboardingAsDismissedUseCase(get(), get()) }
}