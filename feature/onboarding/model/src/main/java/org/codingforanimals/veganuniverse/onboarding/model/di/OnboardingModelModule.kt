package org.codingforanimals.veganuniverse.onboarding.model.di

import org.codingforanimals.veganuniverse.onboarding.data.di.onboardingDataModule
import org.codingforanimals.veganuniverse.onboarding.model.SetOnboardingAsDismissedUseCase
import org.codingforanimals.veganuniverse.onboarding.model.ShowOnboardingUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val onboardingModelModule = module {
    includes(onboardingDataModule)
    factoryOf(::ShowOnboardingUseCase)
    factoryOf(::SetOnboardingAsDismissedUseCase)
}