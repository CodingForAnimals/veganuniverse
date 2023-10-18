package org.codingforanimals.veganuniverse.create.presentation.recipe.di

import org.codingforanimals.veganuniverse.create.presentation.recipe.CreateRecipeViewModel
import org.codingforanimals.veganuniverse.create.presentation.recipe.usecase.SubmitRecipeUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val createRecipePresentationModule = module {
    factoryOf(::SubmitRecipeUseCase)
    viewModelOf(::CreateRecipeViewModel)
}