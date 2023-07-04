package org.codingforanimals.veganuniverse.registration.presentation.signin.di

import org.codingforanimals.veganuniverse.registration.presentation.signin.EmailSignInViewModel
import org.codingforanimals.veganuniverse.registration.presentation.signin.usecase.EmailSignInUseCase
import org.codingforanimals.veganuniverse.registration.presentation.signin.usecase.GetEmailSignInScreenContent
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val signInModule = module {
    factoryOf(::EmailSignInUseCase)
    factoryOf(::GetEmailSignInScreenContent)
    viewModelOf(::EmailSignInViewModel)
}