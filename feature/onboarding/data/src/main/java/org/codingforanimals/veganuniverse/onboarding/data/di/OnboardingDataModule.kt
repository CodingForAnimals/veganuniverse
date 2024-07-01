package org.codingforanimals.veganuniverse.onboarding.data.di

import android.content.Context
import org.codingforanimals.veganuniverse.onboarding.data.OnboardingDataStore
import org.codingforanimals.veganuniverse.onboarding.data.OnboardingLocalStorage
import org.codingforanimals.veganuniverse.onboarding.data.onboardingDataStore
import org.koin.dsl.module

val onboardingDataModule = module {
    factory<OnboardingLocalStorage> {
        OnboardingDataStore(
            dataStore = get<Context>().onboardingDataStore
        )
    }
}
