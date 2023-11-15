package org.codingforanimals.veganuniverse.create.place.domain.di

import org.codingforanimals.veganuniverse.create.place.domain.PlaceCreator
import org.codingforanimals.veganuniverse.create.place.domain.PlaceCreatorImpl
import org.codingforanimals.veganuniverse.places.services.firebase.di.placesFirebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val createPlaceDomainModule = module {
    includes(placesFirebaseServiceModule)
    factoryOf(::PlaceCreatorImpl) bind PlaceCreator::class

}