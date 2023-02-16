package org.codingforanimals.veganuniverse.registration.presentation.di

import org.codingforanimals.veganuniverse.registration.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val registrationModule = module {
    viewModel { RegisterViewModel(get()) }
}