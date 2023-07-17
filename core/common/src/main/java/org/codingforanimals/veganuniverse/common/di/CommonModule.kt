package org.codingforanimals.veganuniverse.common.di

import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProvider
import org.codingforanimals.veganuniverse.common.coroutines.CoroutineDispatcherProviderImpl
import org.koin.dsl.module

val commonModule = module {
    single<CoroutineDispatcherProvider> { CoroutineDispatcherProviderImpl() }
}