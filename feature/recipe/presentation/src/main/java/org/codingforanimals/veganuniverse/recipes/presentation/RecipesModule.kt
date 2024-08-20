package org.codingforanimals.veganuniverse.recipes.presentation

import org.codingforanimals.veganuniverse.recipes.domain.di.recipesDomainModule
import org.codingforanimals.veganuniverse.recipes.presentation.browsing.di.recipesBrowsingModule
import org.codingforanimals.veganuniverse.recipes.presentation.details.di.recipeDetailsModule
import org.codingforanimals.veganuniverse.recipes.presentation.home.di.recipesHomeModule
import org.codingforanimals.veganuniverse.recipes.presentation.listing.di.recipeListingModule
import org.koin.dsl.module

val recipesPresentationModule = module {
    includes(
        recipesBrowsingModule,
        recipesDomainModule,
        recipeDetailsModule,
        recipesHomeModule,
        recipeListingModule
    )
}