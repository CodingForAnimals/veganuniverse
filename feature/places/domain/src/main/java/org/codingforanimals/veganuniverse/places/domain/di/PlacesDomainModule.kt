package org.codingforanimals.veganuniverse.places.domain.di

import org.codingforanimals.veganuniverse.places.domain.PlacesRepository
import org.codingforanimals.veganuniverse.places.domain.PlacesRepositoryImpl
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.koin.dsl.module

val placesDomainModule = module {
    includes(firebaseServiceModule)

    /**
     * Singleton as a temporary measure before implementing cache.
     * Cache will retain
     */
    single<PlacesRepository> { PlacesRepositoryImpl(get()) }
}