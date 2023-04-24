package org.codingforanimals.veganuniverse.places.domain

import org.koin.dsl.module

val placesDomainModule = module {
    factory<PlacesApi> { PlacesFirestoreApi() }
    factory<PlacesRepository> { PlacesRepositoryImpl(get()) }
}