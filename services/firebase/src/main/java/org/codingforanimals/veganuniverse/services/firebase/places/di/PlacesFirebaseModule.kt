package org.codingforanimals.veganuniverse.services.firebase.places.di

import org.codingforanimals.veganuniverse.services.firebase.places.api.PlacesApi
import org.codingforanimals.veganuniverse.services.firebase.places.api.PlacesFirestoreApi
import org.koin.dsl.module

internal val placesFirebaseModule = module {
    factory<PlacesApi> { PlacesFirestoreApi(get()) }
}