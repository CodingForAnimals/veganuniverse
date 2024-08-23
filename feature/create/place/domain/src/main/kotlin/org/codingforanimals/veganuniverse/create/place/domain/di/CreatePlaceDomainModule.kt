package org.codingforanimals.veganuniverse.create.place.domain.di

import org.codingforanimals.veganuniverse.create.place.domain.usecase.SubmitPlace
import org.codingforanimals.veganuniverse.commons.place.domain.di.placeDomainModule
import org.codingforanimals.veganuniverse.commons.user.domain.di.userCommonDomainModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val createPlaceDomainModule = module {
    includes(
        userCommonDomainModule,
        placeDomainModule,
    )

    factoryOf(::SubmitPlace)
}