package org.codingforanimals.veganuniverse.app.di

import org.codingforanimals.veganuniverse.app.MainViewModel
import org.codingforanimals.veganuniverse.onboarding.presentation.di.onboardingModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {
    includes(onboardingModule)
    viewModel { MainViewModel(get()) }
}