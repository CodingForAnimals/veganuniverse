package org.codingforanimals.veganuniverse.services.firebase.di

import org.codingforanimals.veganuniverse.services.firebase.api.PlaceReviewsApi
import org.codingforanimals.veganuniverse.services.firebase.api.PlaceReviewsFirebaseApi
import org.codingforanimals.veganuniverse.services.firebase.api.PlacesApi
import org.codingforanimals.veganuniverse.services.firebase.api.PlacesFirebaseApi
import org.codingforanimals.veganuniverse.services.firebase.entity.mapper.GetPlaceResultToPlaceCardMapper
import org.codingforanimals.veganuniverse.services.firebase.entity.mapper.PlaceFormToPlaceCardMapper
import org.codingforanimals.veganuniverse.services.firebase.entity.mapper.PlaceFormToPlaceMapper
import org.codingforanimals.veganuniverse.services.firebase.entity.mapper.PlaceReviewFormToFirebaseEntityMapper
import org.codingforanimals.veganuniverse.services.firebase.entity.mapper.PlaceReviewToDomainEntityMapper
import org.codingforanimals.veganuniverse.services.firebase.entity.mapper.PlaceToDomainEntityMapper
import org.codingforanimals.veganuniverse.services.firebase.entity.mapper.mapperModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

val placesFirebaseServiceModule = module {
    includes(firebaseServiceModule, mapperModule)
    factory<PlacesApi> {
        PlacesFirebaseApi(
            firestore = get(),
            database = get(),
            storage = get(),
            placeFormToPlaceCardMapper = get(named(PlaceFormToPlaceCardMapper::class.java.simpleName)),
            placeFormToPlaceMapper = get(named(PlaceFormToPlaceMapper::class.java.simpleName)),
            placeToDomainEntityMapper = get(named(PlaceToDomainEntityMapper::class.java.simpleName)),
            getPlaceResultToPlaceCardMapper = get(named(GetPlaceResultToPlaceCardMapper::class.java.simpleName)),
        )
    }

    factory<PlaceReviewsApi> {
        PlaceReviewsFirebaseApi(
            firestore = get(),
            placeReviewToDomainEntityMapper = get(named(PlaceReviewToDomainEntityMapper::class.java.simpleName)),
            placeReviewFormToFirebaseEntityMapper = get(named(PlaceReviewFormToFirebaseEntityMapper::class.java.simpleName)),
        )
    }
}