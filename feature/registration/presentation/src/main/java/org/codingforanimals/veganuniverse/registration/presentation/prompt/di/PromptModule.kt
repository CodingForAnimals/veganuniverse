package org.codingforanimals.veganuniverse.registration.presentation.prompt.di

import org.codingforanimals.veganuniverse.registration.presentation.prompt.PromptScreenViewModel
import org.codingforanimals.veganuniverse.registration.presentation.prompt.usecase.GetPromptScreenContentUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val promptModule = module {
    factoryOf(::GetPromptScreenContentUseCase)
    viewModelOf(::PromptScreenViewModel)
}