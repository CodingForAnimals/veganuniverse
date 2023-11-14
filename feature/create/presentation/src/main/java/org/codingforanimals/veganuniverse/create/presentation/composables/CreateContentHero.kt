package org.codingforanimals.veganuniverse.create.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.ui.icon.Icon

@Composable
internal fun CreateContentHero(
    content: @Composable () -> Unit,
    heroAnchorIcon: Icon,
    heroAnchorColors: HeroAnchorColors = HeroAnchorDefaults.primaryColors(),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp)
        ) {
            content()
        }
        HeroAnchor(
            modifier = Modifier.align(Alignment.BottomCenter),
            icon = heroAnchorIcon,
            colors = heroAnchorColors
        )
    }
}