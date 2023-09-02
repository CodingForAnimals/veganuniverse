package org.codingforanimals.veganuniverse.services.firebase.entity.mapper

import org.codingforanimals.veganuniverse.entity.EntityMapper
import org.codingforanimals.veganuniverse.entity.OneWayEntityMapper
import org.codingforanimals.veganuniverse.places.entity.PlaceForm
import org.codingforanimals.veganuniverse.places.entity.PlaceReviewForm
import org.codingforanimals.veganuniverse.services.firebase.entity.AddressComponents
import org.codingforanimals.veganuniverse.services.firebase.entity.OpeningHours
import org.codingforanimals.veganuniverse.services.firebase.entity.Period
import org.codingforanimals.veganuniverse.services.firebase.entity.Place
import org.codingforanimals.veganuniverse.services.firebase.entity.PlaceCard
import org.codingforanimals.veganuniverse.services.firebase.entity.PlaceReview
import org.codingforanimals.veganuniverse.services.firebase.model.GetPlaceResult
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
        named(AddressComponentsMapper::class.java.simpleName)
    ) { AddressComponentsMapper() }
    factory<OneWayEntityMapper<GetPlaceResult, PlaceCardDomainEntity>>(
        named(GetPlaceResultToPlaceCardMapper::class.java.simpleName)
    ) { GetPlaceResultToPlaceCardMapper(get()) }
    factory<EntityMapper<OpeningHours, OpeningHoursDomainEntity>>(named(OpeningHoursMapper::class.java.simpleName)) {
        OpeningHoursMapper(get(named(PeriodMapper::class.java.simpleName)))
    }
    factory<EntityMapper<Period, PeriodDomainEntity>>(named(PeriodMapper::class.java.simpleName)) { PeriodMapper() }
    factory<OneWayEntityMapper<PlaceForm, PlaceCard>>(named(PlaceFormToPlaceCardMapper::class.java.simpleName)) { PlaceFormToPlaceCardMapper() }
    factory<OneWayEntityMapper<PlaceReviewForm, PlaceReview>>(
        named(
            PlaceReviewFormToFirebaseEntityMapper::class.java.simpleName
        )
    ) { PlaceReviewFormToFirebaseEntityMapper() }
    factory<OneWayEntityMapper<PlaceReview, PlaceReviewDomainEntity>>(
        named(
            PlaceReviewToDomainEntityMapper::class.java.simpleName
        )
    ) { PlaceReviewToDomainEntityMapper() }
    factory<OneWayEntityMapper<PlaceForm, Place>>(named(PlaceFormToPlaceMapper::class.java.simpleName)) {
        PlaceFormToPlaceMapper(
            get(named(OpeningHoursMapper::class.java.simpleName)),
            get(named(AddressComponentsMapper::class.java.simpleName))
        )
    }
    factory<OneWayEntityMapper<Place, PlaceDomainEntity>>(named(PlaceToDomainEntityMapper::class.java.simpleName)) {
        PlaceToDomainEntityMapper(
            context = get(),
            addressComponentsMapper = get(named(AddressComponentsMapper::class.java.simpleName)),
            openingHoursMapper = get(named(OpeningHoursMapper::class.java.simpleName)),
        )
    }
}