package org.codingforanimals.veganuniverse.recipes.presentation.listing.di

import org.codingforanimals.veganuniverse.recipes.presentation.listing.RecipeListingViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val recipeListingModule = module {
    viewModelOf(::RecipeListingViewModel)
}