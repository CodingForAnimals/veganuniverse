package org.codingforanimals.veganuniverse.app.di

import org.codingforanimals.veganuniverse.app.MainViewModel
import org.codingforanimals.veganuniverse.auth.di.authModule
import org.codingforanimals.veganuniverse.core.location.userLocationModule
import org.codingforanimals.veganuniverse.onboarding.presentation.di.onboardingModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val appModule = module {
    includes(
        onboardingModule,
        userLocationModule,
        authModule,
    )
    viewModelOf(::MainViewModel)
}