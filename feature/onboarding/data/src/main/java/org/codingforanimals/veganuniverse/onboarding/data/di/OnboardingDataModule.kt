package org.codingforanimals.veganuniverse.onboarding.data.di

import org.codingforanimals.veganuniverse.onboarding.data.OnboardingDataStoreManager
import org.codingforanimals.veganuniverse.onboarding.data.OnboardingDataStoreManagerImpl
import org.codingforanimals.veganuniverse.datastore.di.dataStoreModule
import org.koin.dsl.module

val onboardingDataModule = module {
    includes(dataStoreModule)
    factory<OnboardingDataStoreManager> { OnboardingDataStoreManagerImpl(get()) }
}