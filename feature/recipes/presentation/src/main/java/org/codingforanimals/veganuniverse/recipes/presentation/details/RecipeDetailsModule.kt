package org.codingforanimals.veganuniverse.recipes.presentation.details

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val recipeDetailsModule = module {
    viewModelOf(::RecipeDetailsViewModel)
}