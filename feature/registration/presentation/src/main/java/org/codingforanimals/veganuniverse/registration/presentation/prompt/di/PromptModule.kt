package org.codingforanimals.veganuniverse.registration.presentation.prompt.di

import org.codingforanimals.veganuniverse.registration.presentation.prompt.PromptScreenViewModel
import org.codingforanimals.veganuniverse.registration.presentation.prompt.usecase.GetPromptScreenContent
import org.codingforanimals.veganuniverse.registration.presentation.prompt.usecase.GmailAuthenticationUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val promptModule = module {
    factoryOf(::GmailAuthenticationUseCase)
    factoryOf(::GetPromptScreenContent)
    viewModelOf(::PromptScreenViewModel)
}