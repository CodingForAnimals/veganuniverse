package org.codingforanimals.veganuniverse.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VUCircularProgressIndicator(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    strokeWidth: Dp = 4.dp,
    clickable: Boolean = true,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    AnimatedVisibility(
        modifier = modifier
            .fillMaxSize()
            .clickable(enabled = clickable, onClick = {}),
        visible = visible,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                strokeWidth = strokeWidth,
                color = color,
            )
        }
    }
}