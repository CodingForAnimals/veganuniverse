package org.codingforanimals.veganuniverse.commons.navigation

import android.net.Uri
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

interface DeeplinkNavigator {
    val deeplinkFlow: Flow<Uri>
    suspend fun navigate(deeplink: DeepLink)
}

internal class DeeplinkNavigatorImpl : DeeplinkNavigator {
    private val internalDeeplinkChannel = Channel<Uri>()
    override val deeplinkFlow: Flow<Uri>
        get() = internalDeeplinkChannel.receiveAsFlow()

    override suspend fun navigate(deeplink: DeepLink) {
        internalDeeplinkChannel.send(Uri.parse(deeplink.deeplink))
    }
}