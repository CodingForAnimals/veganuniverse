package org.codingforanimals.veganuniverse.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import org.codingforanimals.map.presentation.MapFloatingActionButton
import org.codingforanimals.map.presentation.navigation.mapNavigationRoute
import org.codingforanimals.map.presentation.navigation.navigateToMap
import org.codingforanimals.places.presentation.navigation.navigateToPlaces
import org.codingforanimals.places.presentation.navigation.placesNavigationRoute
import org.codingforanimals.veganuniverse.community.presentation.navigation.communityNavigationRoute
import org.codingforanimals.veganuniverse.community.presentation.navigation.navigateToCommunity
import org.codingforanimals.veganuniverse.navigation.TopLevelDestination
import org.codingforanimals.veganuniverse.presentation.navigation.navigateToRecipes
import org.codingforanimals.veganuniverse.presentation.navigation.recipesNavigationRoute

@Composable
internal fun rememberVeganUniverseAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
): VeganUniverseAppState {
    return remember(coroutineScope, navController, cameraPositionState) {
        VeganUniverseAppState(navController, coroutineScope, cameraPositionState)
    }
}

@Stable
class VeganUniverseAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val cameraPositionState: CameraPositionState,
) {

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            communityNavigationRoute -> TopLevelDestination.COMMUNITY
            placesNavigationRoute -> TopLevelDestination.PLACES
            recipesNavigationRoute -> TopLevelDestination.RECIPES
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        when (destination) {
            TopLevelDestination.COMMUNITY -> navController.navigateToCommunity()
            TopLevelDestination.PLACES -> navController.navigateToPlaces()
            TopLevelDestination.RECIPES -> navController.navigateToRecipes()
        }
    }
}