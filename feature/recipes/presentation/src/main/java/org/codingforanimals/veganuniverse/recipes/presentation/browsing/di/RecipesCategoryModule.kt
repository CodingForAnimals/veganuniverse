package org.codingforanimals.veganuniverse.recipes.presentation.browsing.di

import org.codingforanimals.veganuniverse.recipes.presentation.browsing.RecipeBrowsingViewModel
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.usecase.GetRecipesUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val recipesCategoryModule = module {
    factoryOf(::GetRecipesUseCase)
    viewModelOf(::RecipeBrowsingViewModel)
}