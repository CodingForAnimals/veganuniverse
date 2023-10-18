package org.codingforanimals.veganuniverse.recipes.presentation.recipe

import org.codingforanimals.veganuniverse.recipes.presentation.recipe.usecase.GetRecipeUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val recipeModule = module {
    viewModelOf(::RecipeViewModel)
    factoryOf(::GetRecipeUseCase)
}