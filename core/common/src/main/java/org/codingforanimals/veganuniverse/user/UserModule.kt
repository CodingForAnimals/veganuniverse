package org.codingforanimals.veganuniverse.user

import org.koin.dsl.module

val userModule = module {
    single<UserRepository> { UserRepositoryImpl() }
}