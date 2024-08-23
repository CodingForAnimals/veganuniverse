package org.codingforanimals.veganuniverse.commons.navigation

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonsNavigationModule = module {
    singleOf(::DeeplinkNavigatorImpl) bind DeeplinkNavigator::class
}
