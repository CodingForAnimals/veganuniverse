package org.codingforanimals.veganuniverse.create.presentation.di

import org.codingforanimals.veganuniverse.create.domain.di.createFeatureDomainModule
import org.codingforanimals.veganuniverse.create.presentation.place.di.createPlaceModule
import org.koin.dsl.module

val createFeatureModule = module {
    includes(
        createFeatureDomainModule,
        createPlaceModule,
    )
}