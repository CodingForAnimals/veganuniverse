package org.codingforanimals.veganuniverse.commons.navigation.model

import android.net.Uri

data class DeepLinkNavigation(
    val uri: Uri,
    val options: DeepLinkNavigationOptions? = null,
)