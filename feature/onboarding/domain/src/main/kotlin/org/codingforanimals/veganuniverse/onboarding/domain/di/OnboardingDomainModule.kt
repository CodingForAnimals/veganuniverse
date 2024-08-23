package org.codingforanimals.veganuniverse.onboarding.domain.di

import org.codingforanimals.veganuniverse.onboarding.data.di.onboardingDataModule
import org.codingforanimals.veganuniverse.onboarding.domain.OnboardingRepository
import org.codingforanimals.veganuniverse.onboarding.domain.OnboardingRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val onboardingDomainModule = module {
    includes(onboardingDataModule)
    factoryOf(::OnboardingRepositoryImpl) bind OnboardingRepository::class
}
