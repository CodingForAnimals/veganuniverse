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
import org.codingforanimals.places.presentation.navigation.PlacesDestination
import org.codingforanimals.veganuniverse.community.presentation.navigation.CommunityDestination
import org.codingforanimals.veganuniverse.core.ui.navigation.navigate
import org.codingforanimals.veganuniverse.create.presentation.navigation.CreateDestination
import org.codingforanimals.veganuniverse.navigation.TopLevelDestination
import org.codingforanimals.veganuniverse.notifications.presentation.navigation.NotificationsDestination
import org.codingforanimals.veganuniverse.presentation.navigation.RecipesDestination
import org.codingforanimals.veganuniverse.profile.presentation.navigation.ProfileDestination
import org.codingforanimals.veganuniverse.ui.topappbar.TopBarAction

@Composable
internal fun rememberVeganUniverseAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
): VeganUniverseAppState {
    return remember(coroutineScope, navController, cameraPositionState) {
        VeganUniverseAppState(
            navController,
            coroutineScope,
            cameraPositionState,
        )
    }
}

@Stable
class VeganUniverseAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val cameraPositionState: CameraPositionState,
) {

    val topBarActions: List<TopBarAction> = listOf(
        TopBarAction.NotificationTopBarAction,
        TopBarAction.SettingsTopBarAction,
    )

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            CommunityDestination.route -> TopLevelDestination.COMMUNITY
            PlacesDestination.route -> TopLevelDestination.PLACES
            CreateDestination.route -> TopLevelDestination.CREATE
            RecipesDestination.route -> TopLevelDestination.RECIPES
            ProfileDestination.route -> TopLevelDestination.PROFILE
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        when (destination) {
            TopLevelDestination.COMMUNITY -> navController.navigate(CommunityDestination)
            TopLevelDestination.PLACES -> navController.navigate(PlacesDestination)
            TopLevelDestination.CREATE -> navController.navigate(CreateDestination)
            TopLevelDestination.RECIPES -> navController.navigate(RecipesDestination)
            TopLevelDestination.PROFILE -> navController.navigate(ProfileDestination)
        }
    }

    fun navigateBackToCommunity() {
        navController.navigate(CommunityDestination.route) {
            popUpTo(CommunityDestination.route) { inclusive = true }
        }
    }

    fun onActionClick(action: TopBarAction) {
        when (action) {
            TopBarAction.NotificationTopBarAction -> navController.navigate(NotificationsDestination)
            TopBarAction.SettingsTopBarAction -> {}
        }
    }
}