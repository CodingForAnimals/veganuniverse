package org.codingforanimals.veganuniverse.core.ui.animation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import org.codingforanimals.veganuniverse.core.ui.theme.ShimmerBackground

fun Modifier.shimmer() = composed {
    shimmer(veganUniverseShimmer())
}

@Composable
fun ShimmerItem(modifier: Modifier = Modifier) {
    Box(modifier.background(ShimmerBackground))
}

@Composable
private fun veganUniverseShimmer() = rememberShimmer(
    shimmerBounds = ShimmerBounds.View,
    theme = defaultShimmerTheme
)

