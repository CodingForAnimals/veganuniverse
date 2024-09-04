package org.codingforanimals.veganuniverse.validator.navigation

import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import org.codingforanimals.veganuniverse.commons.ui.components.BottomNavBarItem
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.navigation.isRouteInHierarchy

@Composable
fun ValidatorBottomAppBar(
    navController: NavController,
    currentDestination: NavDestination?,
) {
    BottomAppBar {
        ValidatorTopLevelDestination.entries.forEach {
            val isSelected = currentDestination.isRouteInHierarchy(it.destination.route)
            BottomNavBarItem(
                isSelected = isSelected,
                onClick = {
                    if (!isSelected) navController.navigate(it.destination.route) {
                        restoreState = true
                        popUpTo(ValidatorDestination.ROUTE) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    val icon = if (isSelected) it.selectedIcon else it.unselectedIcon
                    VUIcon(icon = icon)
                },
            )
        }
    }
}
