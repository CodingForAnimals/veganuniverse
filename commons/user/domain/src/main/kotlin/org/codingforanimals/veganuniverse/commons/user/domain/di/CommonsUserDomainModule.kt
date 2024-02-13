package org.codingforanimals.veganuniverse.commons.user.domain.di

import org.codingforanimals.veganuniverse.commons.user.data.di.commonsUserDataModule
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.GetUserInfo
import org.codingforanimals.veganuniverse.commons.user.domain.usecase.GetUserInfoImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonsUserDomainModule = module {
    includes(
        commonsUserDataModule,
    )
    factoryOf(::GetUserInfoImpl) bind  GetUserInfo::class
}