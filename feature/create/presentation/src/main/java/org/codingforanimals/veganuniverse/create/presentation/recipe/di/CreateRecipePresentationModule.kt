package org.codingforanimals.veganuniverse.create.presentation.recipe.di

import org.codingforanimals.veganuniverse.create.presentation.recipe.CreateRecipeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val createRecipePresentationModule = module {
    viewModelOf(::CreateRecipeViewModel)
}