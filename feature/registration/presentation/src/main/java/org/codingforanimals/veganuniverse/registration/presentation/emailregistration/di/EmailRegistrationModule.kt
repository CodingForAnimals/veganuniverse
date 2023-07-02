package org.codingforanimals.veganuniverse.registration.presentation.emailregistration.di

import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.EmailRegistrationViewModel
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.usecase.EmailAndPasswordRegistrationUseCase
import org.codingforanimals.veganuniverse.registration.presentation.emailregistration.usecase.GetEmailRegistrationScreenContent
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val emailRegistrationModule = module {
    factoryOf(::GetEmailRegistrationScreenContent)
    factoryOf(::EmailAndPasswordRegistrationUseCase)
    viewModelOf(::EmailRegistrationViewModel)
}