package org.codingforanimals.veganuniverse.profile.domain

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val profileDomainModule = module {
    factoryOf(::ProfileRepositoryImpl) bind ProfileRepository::class
}