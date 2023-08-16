package org.codingforanimals.veganuniverse.create.domain.places.di

import org.codingforanimals.veganuniverse.create.domain.places.PlaceCreator
import org.codingforanimals.veganuniverse.create.domain.places.PlaceCreatorImpl
import org.codingforanimals.veganuniverse.places.services.firebase.di.placesFirebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val createPlaceDomainModule = module {
    includes(placesFirebaseServiceModule)
    factoryOf(::PlaceCreatorImpl) bind PlaceCreator::class

}