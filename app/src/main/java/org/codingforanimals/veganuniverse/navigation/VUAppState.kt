package org.codingforanimals.veganuniverse.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.codingforanimals.veganuniverse.create.graph.CreateDestination
import org.codingforanimals.veganuniverse.place.presentation.navigation.PlaceDestination
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination
import org.codingforanimals.veganuniverse.profile.ProfileDestination
import org.codingforanimals.veganuniverse.recipes.presentation.RecipesDestination

@Composable
internal fun rememberVUAppState(
    navController: NavHostController,
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
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            ProductDestination.Home.route -> TopLevelDestination.PRODUCTS
            PlaceDestination.Home.route -> TopLevelDestination.PLACES
            CreateDestination.Home.route -> TopLevelDestination.CREATE
            RecipesDestination.Home.route -> TopLevelDestination.RECIPES
            ProfileDestination.Home.route -> TopLevelDestination.PROFILE
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        navController.navigate(destination.route) {
            restoreState = true
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
        }
    }

    fun navigateBackHomeToProducts() {
        navController.navigate(ProductDestination.Home.route) {
            popUpTo(ProductDestination.Home.route) { inclusive = true }
        }
    }
}