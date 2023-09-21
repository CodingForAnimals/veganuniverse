package org.codingforanimals.veganuniverse.create.presentation.di

import org.codingforanimals.veganuniverse.create.domain.di.createContentDomainModule
import org.codingforanimals.veganuniverse.create.presentation.place.di.createPlacePresentationModule
import org.codingforanimals.veganuniverse.create.presentation.recipe.di.createRecipePresentationModule
import org.koin.dsl.module

val createContentPresentationModule = module {
    includes(
        createContentDomainModule,
        createPlacePresentationModule,
        createRecipePresentationModule,
    )
}