package org.codingforanimals.veganuniverse.common.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher

interface CoroutineDispatcherProvider {
    fun main(): MainCoroutineDispatcher
    fun default(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
}