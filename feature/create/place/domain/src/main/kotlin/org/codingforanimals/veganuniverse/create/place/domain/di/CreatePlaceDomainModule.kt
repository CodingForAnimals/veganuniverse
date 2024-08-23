package org.codingforanimals.veganuniverse.create.place.domain.di

import org.codingforanimals.veganuniverse.create.place.domain.usecase.SubmitPlace
import org.codingforanimals.veganuniverse.place.domain.di.placeDomainModule
import org.codingforanimals.veganuniverse.user.domain.di.userDomainModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val createPlaceDomainModule = module {
    includes(
        userDomainModule,
        placeDomainModule,
    )

    factoryOf(::SubmitPlace)
}