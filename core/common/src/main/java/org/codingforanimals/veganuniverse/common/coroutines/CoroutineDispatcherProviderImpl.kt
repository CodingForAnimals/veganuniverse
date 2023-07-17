package org.codingforanimals.veganuniverse.common.coroutines

import kotlinx.coroutines.Dispatchers

internal class CoroutineDispatcherProviderImpl : CoroutineDispatcherProvider {
    override fun main() = Dispatchers.Main

    override fun default() = Dispatchers.Default

    override fun io() = Dispatchers.IO
}