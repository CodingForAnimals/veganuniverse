package org.codingforanimals.veganuniverse.recipes.presentation.recipe.di

import org.codingforanimals.veganuniverse.recipes.presentation.recipe.RecipeDetailsViewModel
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.usecase.CollectUserRecipeToggleableStateUseCase
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.usecase.GetRecipeUseCase
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.usecase.UpdateRecipeToggleableStatusUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val recipeDetailsModule = module {
    viewModelOf(::RecipeDetailsViewModel)
    factoryOf(::GetRecipeUseCase)
    factoryOf(::UpdateRecipeToggleableStatusUseCase)
    factoryOf(::CollectUserRecipeToggleableStateUseCase)
}