package org.codingforanimals.veganuniverse.recipes.presentation.details.di

import org.codingforanimals.veganuniverse.recipes.presentation.details.RecipeDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val recipeDetailsModule = module {
    viewModelOf(::RecipeDetailsViewModel)
}
