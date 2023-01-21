package org.codingforanimals.veganuniverse.site.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun SiteScreen() {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit, block = { isVisible = true })
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically { it },
            exit = slideOutVertically { it }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Text(text = "this is site screen")
            }
        }
    }
}