package org.codingforanimals.veganuniverse.recipes.presentation

import org.codingforanimals.veganuniverse.recipes.domain.di.recipesDomainModule
import org.codingforanimals.veganuniverse.recipes.presentation.category.di.recipesCategoryModule
import org.codingforanimals.veganuniverse.recipes.presentation.home.di.recipesHomeModule
import org.codingforanimals.veganuniverse.recipes.presentation.recipe.recipeModule
import org.koin.dsl.module

val recipesPresentationModule = module {
    includes(
        recipesCategoryModule,
        recipesDomainModule,
        recipeModule,
        recipesHomeModule,
    )
}