package org.codingforanimals.veganuniverse.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import org.codingforanimals.veganuniverse.core.ui.components.BottomNavBar
import org.codingforanimals.veganuniverse.core.ui.components.BottomNavBarItem
import org.codingforanimals.veganuniverse.core.ui.icons.Icon
import org.codingforanimals.veganuniverse.navigation.TopLevelDestination


@Composable
internal fun VeganUniverseBottomNavBar(
    modifier: Modifier = Modifier,
    visible: Boolean,
    destinations: List<TopLevelDestination>,
    navigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?
) {
    AnimatedVisibility(visible = visible) {
        BottomNavBar(modifier = modifier) {
            destinations.forEach {
                val isSelected = currentDestination.isTopLevelDestinationInHierarchy(it)
                BottomNavBarItem(
                    isSelected = isSelected,
                    onClick = { if (!isSelected) navigateToDestination(it) },
                    icon = {
                        val icon = if (isSelected) {
                            it.selectedIcon
                        } else {
                            it.unselectedIcon
                        }
                        when (icon) {
                            is Icon.ImageVectorIcon -> Icon(
                                imageVector = icon.imageVector,
                                contentDescription = stringResource(it.iconTextId),
                            )
                            is Icon.DrawableResourceIcon -> Icon(
                                painter = painterResource(icon.id),
                                contentDescription = stringResource(it.iconTextId),
                            )
                        }
                    },
                    label = { Text(stringResource(it.iconTextId)) }
                )
            }
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false