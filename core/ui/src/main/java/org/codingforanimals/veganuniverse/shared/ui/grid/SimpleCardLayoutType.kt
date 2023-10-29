package org.codingforanimals.veganuniverse.shared.ui.grid

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

sealed class SimpleCardLayoutType(val modifier: Modifier) {
    data object Squared : SimpleCardLayoutType(Modifier.aspectRatio(1f))
    data object Vertical : SimpleCardLayoutType(Modifier.fillMaxSize())
}