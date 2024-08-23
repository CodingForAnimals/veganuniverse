package org.codingforanimals.veganuniverse.commons.ui.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.request.CachePolicy

@Composable
fun rememberCoilDiskCacheImageLoader(): ImageLoader {
    val context = LocalContext.current
    return ImageLoader
        .Builder(context)
        .diskCachePolicy(CachePolicy.ENABLED)
        .build()
}
