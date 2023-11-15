package org.codingforanimals.veganuniverse.create.recipe.di

import org.codingforanimals.veganuniverse.create.recipe.CreateRecipeViewModel
import org.codingforanimals.veganuniverse.create.recipe.domain.di.createRecipeDomainModule
import org.codingforanimals.veganuniverse.create.recipe.usecase.SubmitRecipeUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val createRecipePresentationModule = module {
    includes(createRecipeDomainModule)
    factoryOf(::SubmitRecipeUseCase)
    viewModelOf(::CreateRecipeViewModel)
}