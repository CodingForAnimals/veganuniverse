package org.codingforanimals.veganuniverse.app.di

import org.codingforanimals.veganuniverse.app.SplashViewModel
import org.codingforanimals.veganuniverse.onboarding.presentation.di.onboardingModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    includes(onboardingModule)
    viewModel { SplashViewModel(get()) }
}