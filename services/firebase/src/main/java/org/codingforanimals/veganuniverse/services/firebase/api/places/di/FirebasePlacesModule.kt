package org.codingforanimals.veganuniverse.services.firebase.api.places.di

import org.codingforanimals.veganuniverse.services.firebase.api.places.PlacesApi
import org.codingforanimals.veganuniverse.services.firebase.api.places.PlacesFirestoreApi
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.koin.dsl.module

val firebasePlacesModule = module {
    includes(firebaseServiceModule)
    factory<PlacesApi> { PlacesFirestoreApi(get()) }
}