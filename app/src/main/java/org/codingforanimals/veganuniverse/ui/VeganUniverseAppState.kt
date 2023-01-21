package org.codingforanimals.veganuniverse.ui

import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import org.codingforanimals.map.presentation.MapFloatingActionButton
import org.codingforanimals.map.presentation.navigation.mapNavigationRoute
import org.codingforanimals.map.presentation.navigation.navigateToMap
import org.codingforanimals.veganuniverse.community.presentation.navigation.communityNavigationRoute
import org.codingforanimals.veganuniverse.community.presentation.navigation.navigateToCommunity
import org.codingforanimals.veganuniverse.navigation.TopLevelDestination
import org.codingforanimals.veganuniverse.presentation.navigation.navigateToRecipes
import org.codingforanimals.veganuniverse.presentation.navigation.recipesNavigationRoute

@Composable
internal fun rememberVeganUniverseAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): VeganUniverseAppState {
    return remember(coroutineScope, navController) {
        VeganUniverseAppState(navController, coroutineScope)
    }
}

@Stable
class VeganUniverseAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
) {

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            communityNavigationRoute -> TopLevelDestination.COMMUNITY
            mapNavigationRoute -> TopLevelDestination.MAP
            recipesNavigationRoute -> TopLevelDestination.RECIPES
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        when (destination) {
            TopLevelDestination.COMMUNITY -> navController.navigateToCommunity()
            TopLevelDestination.MAP -> navController.navigateToMap()
            TopLevelDestination.RECIPES -> navController.navigateToRecipes()
        }
    }

    @Composable
    fun FloatingActionButton() {
        when (currentTopLevelDestination) {
            TopLevelDestination.MAP -> MapFloatingActionButton()
            else -> Unit
        }
    }
}