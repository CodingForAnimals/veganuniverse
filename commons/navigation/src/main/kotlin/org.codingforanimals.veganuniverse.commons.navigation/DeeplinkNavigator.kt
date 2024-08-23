package org.codingforanimals.veganuniverse.commons.navigation

import android.net.Uri
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

private val internalDeeplinkChannel = Channel<Uri>()

interface DeeplinkNavigator {
    val deeplinkFlow: Flow<Uri>
    suspend fun navigate(deeplink: Deeplink)
}

internal class DeeplinkNavigatorImpl : DeeplinkNavigator {
    override val deeplinkFlow: Flow<Uri>
        get() = internalDeeplinkChannel.receiveAsFlow()

    override suspend fun navigate(deeplink: Deeplink) {
        internalDeeplinkChannel.send(Uri.parse(deeplink.deeplink))
    }
}