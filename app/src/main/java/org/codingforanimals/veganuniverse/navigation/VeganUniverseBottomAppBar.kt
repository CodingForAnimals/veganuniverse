package org.codingforanimals.veganuniverse.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import org.codingforanimals.veganuniverse.commons.ui.components.BottomNavBarItem
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.navigation.isRouteInHierarchy


@Composable
internal fun VeganUniverseBottomAppBar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    destinations: List<TopLevelDestination>,
    navigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
) {
    AnimatedVisibility(visible = visible) {
        BottomAppBar(
            modifier = modifier,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            destinations.forEach {
                val isSelected = currentDestination.isRouteInHierarchy(it.route)
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
                )
            }
        }
    }
}
