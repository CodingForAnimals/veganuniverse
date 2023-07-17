package org.codingforanimals.veganuniverse.places.domain.di

import org.codingforanimals.veganuniverse.places.domain.CurrentPlacesWrapper
import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.places.domain.PlacesRepositoryImpl
import org.codingforanimals.veganuniverse.services.firebase.api.places.di.firebasePlacesModule
import org.koin.dsl.module

val placesDomainModule = module {
    includes(firebasePlacesModule)

    /**
     * Singleton as a temporary measure before implementing cache.
     * Cache will retain
     */
    single { CurrentPlacesWrapper() }
    factory<PlacesRepository> { PlacesRepositoryImpl(get(), get()) }
}