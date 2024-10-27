package org.codingforanimals.veganuniverse.recipes.presentation.create.di

import org.codingforanimals.veganuniverse.recipes.presentation.create.CreateRecipeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val createRecipePresentationModule = module {
    viewModelOf(::CreateRecipeViewModel)
}
