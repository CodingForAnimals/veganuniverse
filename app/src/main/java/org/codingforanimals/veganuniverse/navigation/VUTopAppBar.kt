@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.commons.ui.components.VUMediumTopAppBar

@Composable
internal fun VUTopAppBar(
    modifier: Modifier = Modifier,
    topLevelDestination: TopLevelDestination?,
    onBackClick: () -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    AnimatedVisibility(
        visible = topLevelDestination != null,
    ) {
        VUMediumTopAppBar(
            modifier = modifier,
            title = topLevelDestination?.titleRes?.let { stringResource(it) } ?: "",
            onBackClick = if (topLevelDestination == TopLevelDestination.PRODUCTS) null else onBackClick,
            colors = colors,
        )
    }
}