package org.codingforanimals.veganuniverse.registration.presentation.emailsignin.di

import org.codingforanimals.veganuniverse.registration.presentation.emailsignin.EmailSignInViewModel
import org.codingforanimals.veganuniverse.registration.presentation.emailsignin.usecase.GetEmailSignInScreenContent
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val signInModule = module {
    factoryOf(::GetEmailSignInScreenContent)
    viewModelOf(::EmailSignInViewModel)
}