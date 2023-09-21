package org.codingforanimals.veganuniverse.create.domain.di

import org.codingforanimals.veganuniverse.create.domain.places.di.createPlaceDomainModule
import org.codingforanimals.veganuniverse.create.domain.recipes.di.createRecipeModule
import org.koin.dsl.module

val createContentDomainModule = module {
    includes(
        createPlaceDomainModule,
        createRecipeModule,
    )
}