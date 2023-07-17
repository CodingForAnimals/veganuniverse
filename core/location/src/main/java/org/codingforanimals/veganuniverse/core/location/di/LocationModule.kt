package org.codingforanimals.veganuniverse.core.location.di

import org.codingforanimals.veganuniverse.core.location.UserLocationManager
import org.codingforanimals.veganuniverse.core.location.UserLocationManagerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val locationModule = module {
    singleOf(::UserLocationManagerImpl) bind UserLocationManager::class
}
