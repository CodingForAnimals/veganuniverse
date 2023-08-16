package org.codingforanimals.veganuniverse.create.domain.di

import org.codingforanimals.veganuniverse.create.domain.places.di.createPlaceDomainModule
import org.koin.dsl.module

val createFeatureDomainModule = module {
    includes(
        createPlaceDomainModule,
    )
}