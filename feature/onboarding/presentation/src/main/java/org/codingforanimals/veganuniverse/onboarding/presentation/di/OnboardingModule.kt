package org.codingforanimals.veganuniverse.onboarding.presentation.di

import org.codingforanimals.veganuniverse.onboarding.model.di.onboardingModelModule
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingPresenter
import org.codingforanimals.veganuniverse.onboarding.presentation.OnboardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val onboardingModule = module {
    includes(onboardingModelModule)
    factory { OnboardingPresenter(get(), get()) }
    viewModel { OnboardingViewModel() }
}