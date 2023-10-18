package org.codingforanimals.veganuniverse.recipes.presentation.home.di

import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeViewModel
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.di.recipeCarouselModule
import org.codingforanimals.veganuniverse.recipes.presentation.home.tagcontainer.di.recipeTagContainerModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val recipesHomeModule = module {
    includes(
        recipeCarouselModule,
        recipeTagContainerModule,
    )
    viewModelOf(::RecipesHomeViewModel)
}