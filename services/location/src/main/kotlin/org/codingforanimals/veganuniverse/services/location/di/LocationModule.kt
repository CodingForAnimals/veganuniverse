package org.codingforanimals.veganuniverse.services.location.di

import org.codingforanimals.veganuniverse.services.location.UserLocationManager
import org.codingforanimals.veganuniverse.services.location.UserLocationManagerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val locationModule = module {
    singleOf(::UserLocationManagerImpl) bind UserLocationManager::class
}
