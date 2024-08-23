package org.codingforanimals.veganuniverse.commons.navigation

import android.net.Uri
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import org.codingforanimals.veganuniverse.commons.navigation.model.DeepLinkNavigation
import org.codingforanimals.veganuniverse.commons.navigation.model.DeepLinkNavigationOptions

interface DeeplinkNavigator {
    val deeplinkFlow: Flow<DeepLinkNavigation>
    suspend fun navigate(deeplink: DeepLink, options: DeepLinkNavigationOptions? = null)
}

internal class DeeplinkNavigatorImpl : DeeplinkNavigator {
    private val internalDeeplinkChannel = Channel<DeepLinkNavigation>()
    override val deeplinkFlow: Flow<DeepLinkNavigation>
        get() = internalDeeplinkChannel.receiveAsFlow()

    override suspend fun navigate(deeplink: DeepLink, options: DeepLinkNavigationOptions?) {
        val navigation = DeepLinkNavigation(Uri.parse(deeplink.deeplink), options)
        internalDeeplinkChannel.send(navigation)
    }
}
