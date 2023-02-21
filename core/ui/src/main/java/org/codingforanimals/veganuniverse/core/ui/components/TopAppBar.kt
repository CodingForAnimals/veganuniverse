@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VeganUniverseMediumTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    actions: @Composable RowScope.() -> Unit,
    navigationIcon: @Composable () -> Unit,
    colors: TopAppBarColors,
) {
    MediumTopAppBar(
        modifier = modifier,
        title = { Text(title) },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
    )
}