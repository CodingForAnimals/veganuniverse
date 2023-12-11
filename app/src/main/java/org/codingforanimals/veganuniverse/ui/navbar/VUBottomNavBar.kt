package org.codingforanimals.veganuniverse.ui.navbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import org.codingforanimals.veganuniverse.navigation.TopLevelDestination
import org.codingforanimals.veganuniverse.ui.components.BottomNavBar
import org.codingforanimals.veganuniverse.ui.components.BottomNavBarItem
import org.codingforanimals.veganuniverse.ui.icon.Icon


@Composable
internal fun VUBottomNavBar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    destinations: List<TopLevelDestination>,
    navigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
) {
    AnimatedVisibility(visible = visible) {
        BottomNavBar(modifier = modifier) {
            destinations.forEach {
                val isSelected = currentDestination.isTopLevelDestinationInHierarchy(it)
                BottomNavBarItem(
                    isSelected = isSelected,
                    onClick = { if (!isSelected) navigateToDestination(it) },
                    icon = {
                        Crossfade(
                            targetState = isSelected,
                            label = "nav_bar_icon_animation"
                        ) { isSelected ->
                            when (val icon =
                                if (isSelected) it.selectedIcon else it.unselectedIcon) {
                                is Icon.ImageVectorIcon -> {
                                    Icon(
                                        modifier = Modifier.size(24.dp),
                                        imageVector = icon.imageVector,
                                        contentDescription = stringResource(it.iconTextId),
                                    )
                                }

                                is Icon.DrawableResourceIcon -> {
                                    Icon(
                                        modifier = Modifier.size(24.dp),
                                        painter = painterResource(icon.id),
                                        contentDescription = stringResource(it.iconTextId),
                                    )
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.route, true) ?: false
    } ?: false