package org.codingforanimals.veganuniverse.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun VUCircularProgressIndicator(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
) {
    AnimatedVisibility(modifier = modifier
        .fillMaxSize()
        .clickable {}, visible = visible) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}