package org.codingforanimals.veganuniverse.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import org.koin.dsl.module

private class CoroutineDispatcherProviderImpl : CoroutineDispatcherProvider {
    override fun main() = Dispatchers.Main

    override fun default() = Dispatchers.Default

    override fun io() = Dispatchers.IO

}

interface CoroutineDispatcherProvider {
    fun main(): MainCoroutineDispatcher
    fun default(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
}

val coroutineDispatcherModule = module {
    single<CoroutineDispatcherProvider> { CoroutineDispatcherProviderImpl() }
}