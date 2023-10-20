package org.codingforanimals.veganuniverse.places.services.di

import org.codingforanimals.veganuniverse.places.services.PlaceReviewsFirebaseService
import org.codingforanimals.veganuniverse.places.services.PlaceReviewsService
import org.codingforanimals.veganuniverse.places.services.PlacesFirebaseService
import org.codingforanimals.veganuniverse.places.services.PlacesService
import org.codingforanimals.veganuniverse.places.services.entity.mapper.GET_PLACE_RESULT_TO_PLACE_CARD_MAPPER
import org.codingforanimals.veganuniverse.places.services.entity.mapper.PLACE_FORM_TO_PLACE_CARD_FIREBASE_ENTITY_MAPPER
import org.codingforanimals.veganuniverse.places.services.entity.mapper.PLACE_FORM_TO_PLACE_MAPPER
import org.codingforanimals.veganuniverse.places.services.entity.mapper.PLACE_REVIEW_FORM_TO_FIREBASE_ENTITY_MAPPER
import org.codingforanimals.veganuniverse.places.services.entity.mapper.PLACE_REVIEW_TO_DOMAIN_ENTITY_MAPPER
import org.codingforanimals.veganuniverse.places.services.entity.mapper.PLACE_TO_DOMAIN_ENTITY_MAPPER
import org.codingforanimals.veganuniverse.places.services.entity.mapper.mapperModule
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

val placesFirebaseServiceModule = module {
    includes(
        firebaseServiceModule,
        mapperModule,
    )
    factory<PlacesService> {
        PlacesFirebaseService(
            firestore = get(),
            database = get(),
            storage = get(),
            placeFormToPlaceCardMapper = get(named(PLACE_FORM_TO_PLACE_CARD_FIREBASE_ENTITY_MAPPER)),
            placeFormToPlaceMapper = get(named(PLACE_FORM_TO_PLACE_MAPPER)),
            placeToDomainEntityMapper = get(named(PLACE_TO_DOMAIN_ENTITY_MAPPER)),
            getPlaceResultToPlaceCardMapper = get(named(GET_PLACE_RESULT_TO_PLACE_CARD_MAPPER)),
        )
    }

    factory<PlaceReviewsService> {
        PlaceReviewsFirebaseService(
            firestore = get(),
            placeReviewToDomainEntityMapper = get(named(PLACE_REVIEW_TO_DOMAIN_ENTITY_MAPPER)),
            placeReviewFormToFirebaseEntityMapper = get(
                named(PLACE_REVIEW_FORM_TO_FIREBASE_ENTITY_MAPPER)
            ),
        )
    }
}