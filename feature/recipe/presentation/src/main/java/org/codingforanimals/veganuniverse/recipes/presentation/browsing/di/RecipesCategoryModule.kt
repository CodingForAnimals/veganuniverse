package org.codingforanimals.veganuniverse.recipes.presentation.browsing.di

import org.codingforanimals.veganuniverse.recipes.presentation.browsing.RecipeBrowsingViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val recipesBrowsingModule = module {
    viewModelOf(::RecipeBrowsingViewModel)
}
