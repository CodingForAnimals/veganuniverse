package org.codingforanimals.veganuniverse.places.services.firebase.entity.mapper

import org.codingforanimals.veganuniverse.entity.EntityMapper
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.places.entity.PlaceForm
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.places.services.firebase.entity.AddressComponents
import org.codingforanimals.veganuniverse.places.services.firebase.entity.OpeningHours
import org.codingforanimals.veganuniverse.places.services.firebase.entity.Period
import org.codingforanimals.veganuniverse.places.services.firebase.entity.Place
import org.codingforanimals.veganuniverse.places.services.firebase.entity.PlaceCard
import org.codingforanimals.veganuniverse.places.services.firebase.entity.PlaceReview
import org.codingforanimals.veganuniverse.places.services.firebase.model.GetPlaceResult
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.codingforanimals.veganuniverse.places.entity.AddressComponents as AddressComponentsDomainEntity
import org.codingforanimals.veganuniverse.places.entity.OpeningHours as OpeningHoursDomainEntity
import org.codingforanimals.veganuniverse.places.entity.Period as PeriodDomainEntity
import org.codingforanimals.veganuniverse.places.entity.Place as PlaceDomainEntity
import org.codingforanimals.veganuniverse.places.entity.PlaceCard as PlaceCardDomainEntity
import org.codingforanimals.veganuniverse.places.entity.PlaceReview as PlaceReviewDomainEntity

internal val mapperModule = module {
    factory<EntityMapper<AddressComponents, AddressComponentsDomainEntity>>(
        named(ADDRESS_COMPONENTS_MAPPER)
    ) { AddressComponentsMapper() }

    factory<OneWayEntityMapper<GetPlaceResult, PlaceCardDomainEntity>>(
        named(GET_PLACE_RESULT_TO_PLACE_CARD_MAPPER)
    ) {
        GetPlaceResultToPlaceCardMapper(
            get()
        )
    }

    factory<EntityMapper<OpeningHours, OpeningHoursDomainEntity>>(
        named(OPENING_HOURS_MAPPER)
    ) {
        OpeningHoursMapper(get(named(PERIOD_MAPPER)))
    }

    factory<EntityMapper<Period, PeriodDomainEntity>>(
        named(PERIOD_MAPPER)
    ) { PeriodMapper() }

    factory<OneWayEntityMapper<PlaceForm, PlaceCard>>(
        named(PLACE_FORM_TO_PLACE_CARD_FIREBASE_ENTITY_MAPPER)
    ) { PlaceFormToPlaceCardFirebaseEntityMapper() }

    factory<OneWayEntityMapper<PlaceReviewForm, PlaceReview>>(
        named(PLACE_REVIEW_FORM_TO_FIREBASE_ENTITY_MAPPER)
    ) { PlaceReviewFormToFirebaseEntityMapper() }

    factory<OneWayEntityMapper<PlaceReview, PlaceReviewDomainEntity>>(
        named(PLACE_REVIEW_TO_DOMAIN_ENTITY_MAPPER)
    ) { PlaceReviewToDomainEntityMapper() }

    factory<OneWayEntityMapper<PlaceForm, Place>>(
        named(PLACE_FORM_TO_PLACE_MAPPER)
    ) {
        PlaceFormToPlaceMapper(
            get(named(OPENING_HOURS_MAPPER)),
            get(named(ADDRESS_COMPONENTS_MAPPER))
        )
    }
    factory<OneWayEntityMapper<Place, PlaceDomainEntity>>(
        named(PLACE_TO_DOMAIN_ENTITY_MAPPER)
    ) {
        PlaceToDomainEntityMapper(
            context = get(),
            addressComponentsMapper = get(named(ADDRESS_COMPONENTS_MAPPER)),
            openingHoursMapper = get(named(OPENING_HOURS_MAPPER)),
        )
    }
}

internal const val GET_PLACE_RESULT_TO_PLACE_CARD_MAPPER = "get-place-result-to-place-card-mapper"
internal const val PERIOD_MAPPER = "period-mapper"
internal const val PLACE_FORM_TO_PLACE_CARD_FIREBASE_ENTITY_MAPPER =
    "place-form-to-place-card-firebase-entity-mapper"
internal const val PLACE_REVIEW_FORM_TO_FIREBASE_ENTITY_MAPPER =
    "place-review-form-to-firebase-entity-mapper"
internal const val PLACE_REVIEW_TO_DOMAIN_ENTITY_MAPPER = "place-review-to-domain-entity-mapper"
internal const val PLACE_FORM_TO_PLACE_MAPPER = "place-form-to-place-mapper"
internal const val OPENING_HOURS_MAPPER = "opening-hours-mapper"
internal const val ADDRESS_COMPONENTS_MAPPER = "address-components-mapper"
internal const val PLACE_TO_DOMAIN_ENTITY_MAPPER = "place-to-domain-entity-mapper"