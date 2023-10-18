package org.codingforanimals.veganuniverse.recipes.presentation.category.di

import org.codingforanimals.veganuniverse.recipes.presentation.category.RecipeBrowsingViewModel
import org.codingforanimals.veganuniverse.recipes.presentation.category.usecase.GetRecipesUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val recipesCategoryModule = module {
    factoryOf(::GetRecipesUseCase)
    viewModelOf(::RecipeBrowsingViewModel)
}