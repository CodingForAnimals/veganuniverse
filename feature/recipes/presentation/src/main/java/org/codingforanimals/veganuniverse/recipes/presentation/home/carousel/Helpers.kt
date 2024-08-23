package org.codingforanimals.veganuniverse.recipes.presentation.home.carousel

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView

@Composable
internal fun cardWidth(): Double {
    val view = LocalView.current
    return view.width / 2.7
}