package org.codingforanimals.veganuniverse.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RowScope.BottomNavBarItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
) {
    NavigationBarItem(
        modifier = modifier,
        selected = isSelected,
        onClick = onClick,
        icon = icon,
//        label = label,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = VeganUniverseNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = VeganUniverseNavigationDefaults.navigationContentColor(),
            selectedTextColor = VeganUniverseNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = VeganUniverseNavigationDefaults.navigationContentColor(),
            indicatorColor = VeganUniverseNavigationDefaults.navigationIndicatorColor()
        )
    )
}

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        content = content,
        contentColor = VeganUniverseNavigationDefaults.navigationContentColor(),
    )
}

object VeganUniverseNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationContainerColor() = MaterialTheme.colorScheme.surface

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}