package org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.di

import org.codingforanimals.veganuniverse.di.coreUiModule
import org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.RecipeTagContainerViewModel
import org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.usecase.GetContainerRecipesUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val recipeTagContainerModule = module {
    includes(coreUiModule)
    viewModelOf(::RecipeTagContainerViewModel)
    factoryOf(::GetContainerRecipesUseCase)
}