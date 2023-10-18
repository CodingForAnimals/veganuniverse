package org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.di

import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.RecipeCarouselViewModel
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.carouselcard.RecipeCarouselAsyncCardsViewModel
import org.codingforanimals.veganuniverse.recipes.presentation.home.carousel.usecase.GetCarouselRecipesUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val recipeCarouselModule = module {
    viewModelOf(::RecipeCarouselViewModel)
    viewModelOf(::RecipeCarouselAsyncCardsViewModel)
    factoryOf(::GetCarouselRecipesUseCase)
}