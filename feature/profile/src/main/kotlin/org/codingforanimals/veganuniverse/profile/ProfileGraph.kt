package org.codingforanimals.veganuniverse.profile

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.codingforanimals.veganuniverse.profile.itemlist.presentation.ProfileItemListScreen
import org.codingforanimals.veganuniverse.profile.itemlist.presentation.ProfileItemListViewModel
import org.codingforanimals.veganuniverse.ui.navigation.Destination

sealed class ProfileDestination(route: String) : Destination(route = route) {
    data object Home : ProfileDestination("profile_home_screen")
    data object ItemList : ProfileDestination("profile_itemlist_screen")
}

fun NavGraphBuilder.profileGraph(
    navigateToRegister: () -> Unit,
    navigateToRecipe: (String) -> Unit,
    navigateToPlace: (String) -> Unit,
    navController: NavController,
) {
    composable(
        route = ProfileDestination.Home.route
    ) {
        Text("Profile screen")
    }

    composable(
        route = "${ProfileDestination.ItemList.route}/{${ProfileItemListViewModel.NAV_ARG_SAVEABLE_TYPE}}/{${ProfileItemListViewModel.NAV_ARG_CONTENT_TYPE}}",
        arguments = listOf(
            navArgument(ProfileItemListViewModel.NAV_ARG_SAVEABLE_TYPE) {
                type = NavType.StringType
            },
            navArgument(ProfileItemListViewModel.NAV_ARG_CONTENT_TYPE) {
                type = NavType.StringType
            }
        )
    ) {
        ProfileItemListScreen(
            navigateUp = {},
            navigateToRecipe = navigateToRecipe,
            navigateToPlace = navigateToPlace,
        )
    }
}

