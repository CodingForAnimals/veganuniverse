package org.codingforanimals.veganuniverse.places.domain.di

import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.places.domain.PlacesRepositoryImpl
import org.codingforanimals.veganuniverse.places.services.firebase.di.placesFirebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val placesDomainModule = module {
    includes(placesFirebaseServiceModule)
    factoryOf(::PlacesRepositoryImpl) bind PlacesRepository::class
}