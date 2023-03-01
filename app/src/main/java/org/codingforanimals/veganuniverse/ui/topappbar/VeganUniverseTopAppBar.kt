@file:OptIn(ExperimentalMaterial3Api::class)

package org.codingforanimals.veganuniverse.ui.topappbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.components.VUIcon
import org.codingforanimals.veganuniverse.core.ui.components.VeganUniverseMediumTopAppBar
import org.codingforanimals.veganuniverse.core.ui.icons.VeganUniverseIcons
import org.codingforanimals.veganuniverse.navigation.TopLevelDestination

@Composable
internal fun VeganUniverseTopAppBar(
    modifier: Modifier = Modifier,
    topLevelDestination: TopLevelDestination?,
    onBackClick: () -> Unit = {},
    actions: List<TopBarAction> = emptyList(),
    onActionClick: (TopBarAction) -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    AnimatedVisibility(visible = topLevelDestination != null) {
        VeganUniverseMediumTopAppBar(
            modifier = modifier,
            title = topLevelDestination?.titleRes?.let { stringResource(it) } ?: "",
            navigationIcon = {
                if (topLevelDestination == TopLevelDestination.COMMUNITY) return@VeganUniverseMediumTopAppBar
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = VeganUniverseIcons.ArrowBack, contentDescription = "Volver")
                }
            },
            actions = {
                actions.forEach { action ->
                    IconButton(onClick = { onActionClick(action) }) {
                        VUIcon(
                            modifier = Modifier.size(20.dp),
                            icon = action.icon,
                            contentDescription = action.contentDescription,
                        )
                    }
                }
            },
            colors = colors,
        )
    }
}