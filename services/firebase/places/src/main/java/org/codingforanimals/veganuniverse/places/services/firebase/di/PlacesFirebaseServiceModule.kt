package org.codingforanimals.veganuniverse.places.services.firebase.di

import org.codingforanimals.veganuniverse.places.services.firebase.api.PlaceReviewsApi
import org.codingforanimals.veganuniverse.places.services.firebase.api.PlaceReviewsFirebaseApi
import org.codingforanimals.veganuniverse.places.services.firebase.api.PlacesApi
import org.codingforanimals.veganuniverse.places.services.firebase.api.PlacesFirebaseApi
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val placesFirebaseServiceModule = module {
    includes(firebaseServiceModule)
    factoryOf(::PlacesFirebaseApi) bind PlacesApi::class
    factoryOf(::PlaceReviewsFirebaseApi) bind PlaceReviewsApi::class
}