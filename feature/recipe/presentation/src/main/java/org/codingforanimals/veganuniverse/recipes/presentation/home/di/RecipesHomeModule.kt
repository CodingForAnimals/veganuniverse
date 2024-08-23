package org.codingforanimals.veganuniverse.recipes.presentation.home.di

import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val recipesHomeModule = module {
    viewModelOf(::RecipesHomeViewModel)
}
