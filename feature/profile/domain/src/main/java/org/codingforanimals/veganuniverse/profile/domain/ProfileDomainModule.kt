package org.codingforanimals.veganuniverse.profile.domain

import org.codingforanimals.veganuniverse.user.services.firebase.di.userFirebaseModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val profileDomainModule = module {
    includes(
        userFirebaseModule,
        org.codingforanimals.veganuniverse.places.services.di.placesFirebaseServiceModule
    )
    factoryOf(::ProfileRepositoryImpl) bind ProfileRepository::class
}