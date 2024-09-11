package org.codingforanimals.veganuniverse.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.codingforanimals.veganuniverse.commons.ui.components.BottomNavBarItem
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon


@Composable
internal fun VeganUniverseBottomAppBar(
    modifier: Modifier = Modifier,
    navigateToDestination: (TopLevelDestination) -> Unit,
    selectedDestination: TopLevelDestination,
) {
    BottomAppBar(
        modifier = modifier,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        TopLevelDestination.entries.forEach {
            val isSelected = selectedDestination == it
            BottomNavBarItem(
                isSelected = isSelected,
                onClick = { if (!isSelected) navigateToDestination(it) },
                icon = {
                    Crossfade(
                        targetState = isSelected,
                        label = "nav_bar_icon_animation"
                    ) { isSelected ->
                        val icon = if (isSelected) it.selectedIcon else it.unselectedIcon
                        VUIcon(icon = icon)
                    }
                },
                label = stringResource(it.titleRes)
            )
        }
    }
}
