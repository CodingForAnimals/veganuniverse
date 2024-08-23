package org.codingforanimals.veganuniverse.product.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeScreen
import org.codingforanimals.veganuniverse.product.presentation.list.ProductListScreen
import org.codingforanimals.veganuniverse.product.presentation.list.ProductListViewModel
import org.codingforanimals.veganuniverse.ui.navigation.Destination

sealed class ProductDestination(route: String) : Destination(route) {
    data object Home : ProductDestination("product_home_route")
    data object List : ProductDestination("product_list_route")
}

fun NavGraphBuilder.productGraph(
    navController: NavController,
    navigateToCreateProductScreen: () -> Unit,
    navigateToAuthScreen: (ProductDestination) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    composable(
        route = ProductDestination.Home.route,
    ) {
        ProductHomeScreen(
            snackbarHostState = snackbarHostState,
            navigateToCategoryListScreen = { categoryName ->
                navController.navigate("${ProductDestination.List.route}/$categoryName")
            },
            navigateToCreateProductScreen = navigateToCreateProductScreen,
            navigateToAuthScreen = { navigateToAuthScreen(ProductDestination.Home) },
        )
    }

    composable(
        route = "${ProductDestination.List.route}/{${ProductListViewModel.CATEGORY_ARG}}",
        arguments = listOf(
            navArgument(ProductListViewModel.CATEGORY_ARG) {
                type = NavType.StringType
                nullable = true
            }
        )
    ) {
        ProductListScreen(
            navigateUp = navController::navigateUp,
            navigateToAuthScreen = { navigateToAuthScreen(ProductDestination.List) }
        )
    }
}