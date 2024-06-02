package org.codingforanimals.veganuniverse.create.recipe.di

import org.codingforanimals.veganuniverse.create.recipe.CreateRecipeViewModel
import org.codingforanimals.veganuniverse.create.recipe.domain.di.createRecipeDomainModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val createRecipePresentationModule = module {
    includes(
        createRecipeDomainModule,
    )
    viewModelOf(::CreateRecipeViewModel)
}