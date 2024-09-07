package org.codingforanimals.veganuniverse.validator.navigation

import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import org.codingforanimals.veganuniverse.commons.ui.components.BottomNavBarItem
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.navigation.isRouteInHierarchy

@Composable
fun ValidatorBottomAppBar(
    navigateToDestination: (String) -> Unit,
    currentDestination: NavDestination?,
) {
    BottomAppBar {
        ValidatorTopLevelDestination.entries.forEach {
            val isSelected = currentDestination.isRouteInHierarchy(it.destination.route)
            BottomNavBarItem(
                isSelected = isSelected,
                onClick = {
                    if (!isSelected) navigateToDestination(it.destination.route)
                },
                icon = {
                    val icon = if (isSelected) it.selectedIcon else it.unselectedIcon
                    VUIcon(icon = icon)
                },
            )
        }
    }
}
