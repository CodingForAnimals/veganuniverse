package org.codingforanimals.veganuniverse.places.domain.di

import org.codingforanimals.veganuniverse.places.domain.CurrentPlacesWrapper
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.places.domain.PlacesRepositoryImpl
import org.codingforanimals.veganuniverse.services.firebase.api.places.di.firebasePlacesModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val placesDomainModule = module {
    includes(firebasePlacesModule)

    /**
     * Singleton as a temporary measure before implementing cache.
     * Cache will retain
     */
    singleOf(::CurrentPlacesWrapper)
    factoryOf(::PlacesRepositoryImpl) bind PlacesRepository::class
}