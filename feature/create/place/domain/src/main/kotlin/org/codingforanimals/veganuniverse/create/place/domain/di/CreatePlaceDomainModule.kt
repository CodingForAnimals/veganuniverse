package org.codingforanimals.veganuniverse.create.place.domain.di

import org.codingforanimals.veganuniverse.create.place.domain.usecase.SubmitPlace
import org.codingforanimals.veganuniverse.commons.place.domain.di.placeDomainModule
import org.codingforanimals.veganuniverse.commons.profile.domain.di.PROFILE_PLACE_USE_CASES
import org.codingforanimals.veganuniverse.commons.user.domain.di.userCommonDomainModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val createPlaceDomainModule = module {
    includes(
        userCommonDomainModule,
        placeDomainModule,
    )

    factory {
        SubmitPlace(
            placeRepository = get(),
            flowOnCurrentUser = get(),
            profilePlaceUseCases = get(named(PROFILE_PLACE_USE_CASES))
        )
    }
}