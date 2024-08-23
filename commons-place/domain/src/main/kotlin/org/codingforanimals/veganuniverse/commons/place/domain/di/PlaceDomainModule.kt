package org.codingforanimals.veganuniverse.commons.place.domain.di

import org.codingforanimals.veganuniverse.commons.place.data.di.placeDataModule
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceRepository
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceRepositoryImpl
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceReviewRepository
import org.codingforanimals.veganuniverse.commons.place.domain.repository.PlaceReviewRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val placeDomainModule = module {
    includes(
        placeDataModule,
    )

    factoryOf(::PlaceRepositoryImpl) bind PlaceRepository::class
    factoryOf(::PlaceReviewRepositoryImpl) bind PlaceReviewRepository::class
}