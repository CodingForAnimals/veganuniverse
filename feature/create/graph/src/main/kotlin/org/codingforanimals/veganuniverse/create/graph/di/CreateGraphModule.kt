package org.codingforanimals.veganuniverse.create.graph.di

import org.codingforanimals.veganuniverse.create.home.persentation.di.createHomePresentationModule
import org.codingforanimals.veganuniverse.create.place.presentation.di.createPlacePresentationModule
import org.codingforanimals.veganuniverse.create.recipe.di.createRecipePresentationModule
import org.koin.dsl.module

val createFeatureModule = module {
    includes(
        createHomePresentationModule,
        createPlacePresentationModule,
        createRecipePresentationModule,
    )
}