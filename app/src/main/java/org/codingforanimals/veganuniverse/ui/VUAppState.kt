package org.codingforanimals.veganuniverse.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.codingforanimals.veganuniverse.community.presentation.navigation.CommunityDestination
import org.codingforanimals.veganuniverse.create.presentation.navigation.CreateDestination
import org.codingforanimals.veganuniverse.navigation.TopLevelDestination
import org.codingforanimals.veganuniverse.navigation.rememberVUNavController
import org.codingforanimals.veganuniverse.notifications.presentation.navigation.NotificationsDestination
import org.codingforanimals.veganuniverse.places.presentation.navigation.PlacesDestination
import org.codingforanimals.veganuniverse.profile.presentation.navigation.ProfileDestination
import org.codingforanimals.veganuniverse.recipes.presentation.home.RecipesHomeDestination
import org.codingforanimals.veganuniverse.search.presentation.navigation.SearchDestination
import org.codingforanimals.veganuniverse.settings.presentation.navigation.SettingsDestination
import org.codingforanimals.veganuniverse.ui.topappbar.TopBarAction

@Composable
internal fun rememberVUAppState(
    navController: NavHostController = rememberVUNavController(),
): VUAppState {
    return remember(navController) {
        VUAppState(
            navController,
        )
    }
}

@Stable
class VUAppState(
    val navController: NavHostController,
) {

    val topBarActions: List<TopBarAction> = listOf(
        TopBarAction.SearchTopBarAction,
        TopBarAction.NotificationTopBarAction,
        TopBarAction.SettingsTopBarAction,
    )

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            CommunityDestination.route -> TopLevelDestination.COMMUNITY
            PlacesDestination.Home.route -> TopLevelDestination.PLACES
            CreateDestination.route -> TopLevelDestination.CREATE
            RecipesHomeDestination.route -> TopLevelDestination.RECIPES
            ProfileDestination.route -> TopLevelDestination.PROFILE
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        when (destination) {
            TopLevelDestination.COMMUNITY -> navigateToCommunity()
            TopLevelDestination.PLACES -> navController.navigate(PlacesDestination.Home.route)
            TopLevelDestination.CREATE -> navController.navigate(CreateDestination.route)
            TopLevelDestination.RECIPES -> navController.navigate(RecipesHomeDestination.route)
            TopLevelDestination.PROFILE -> navController.navigate(ProfileDestination.route)
        }
    }

    fun navigateToCommunity() {
        navController.navigate(CommunityDestination.route) {
            popUpTo(CommunityDestination.route) { inclusive = true }
        }
    }

    fun onActionClick(action: TopBarAction) {
        when (action) {
            TopBarAction.SearchTopBarAction -> navController.navigate(SearchDestination.route)
            TopBarAction.NotificationTopBarAction -> navController.navigate(NotificationsDestination.route)
            TopBarAction.SettingsTopBarAction -> navController.navigate(SettingsDestination.route)
        }
    }
}