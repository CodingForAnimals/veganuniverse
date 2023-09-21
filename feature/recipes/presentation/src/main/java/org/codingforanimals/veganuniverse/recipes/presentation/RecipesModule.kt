package org.codingforanimals.veganuniverse.recipes.presentation

import org.codingforanimals.veganuniverse.recipes.domain.di.recipesDomainModule
import org.codingforanimals.veganuniverse.recipes.presentation.details.recipeDetailsModule
import org.koin.dsl.module

val recipesPresentationModule = module {
    includes(
        recipesDomainModule,
        recipeDetailsModule,
    )
}