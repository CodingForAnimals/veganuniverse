package org.codingforanimals.veganuniverse.commons.user.data.di

import org.codingforanimals.veganuniverse.commons.user.data.source.UserInfoDataSource
import org.codingforanimals.veganuniverse.commons.user.data.source.UserInfoFirestoreDataSource
import org.codingforanimals.veganuniverse.services.firebase.di.firebaseServiceModule
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonsUserDataModule = module {
    includes(firebaseServiceModule)
    factoryOf(::UserInfoFirestoreDataSource) bind UserInfoDataSource::class
}