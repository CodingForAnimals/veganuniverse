package org.codingforanimals.veganuniverse.registration.presentation.emailvalidation.di

import org.codingforanimals.veganuniverse.registration.presentation.emailvalidation.EmailValidationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val emailValidationModule = module {
    viewModelOf(::EmailValidationViewModel)
}