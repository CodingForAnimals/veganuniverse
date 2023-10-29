package org.codingforanimals.veganuniverse.profile.domain.di

import org.codingforanimals.veganuniverse.profile.domain.BookmarksRepository
import org.codingforanimals.veganuniverse.profile.domain.ContributionsRepository
import org.codingforanimals.veganuniverse.profile.domain.ProfileRepository
import org.codingforanimals.veganuniverse.profile.domain.impl.BookmarksRepositoryImpl
import org.codingforanimals.veganuniverse.profile.domain.impl.ContributionsRepositoryImpl
import org.codingforanimals.veganuniverse.profile.domain.impl.ProfileRepositoryImpl
import org.codingforanimals.veganuniverse.profile.services.firebase.di.profileFirebaseServiceModule
import org.codingforanimals.veganuniverse.user.services.firebase.di.userFirebaseModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val profileDomainModule = module {
    includes(
        userFirebaseModule,
        profileFirebaseServiceModule
    )
    factoryOf(::ProfileRepositoryImpl) bind ProfileRepository::class
    factoryOf(::BookmarksRepositoryImpl) bind BookmarksRepository::class
    factoryOf(::ContributionsRepositoryImpl) bind ContributionsRepository::class
}