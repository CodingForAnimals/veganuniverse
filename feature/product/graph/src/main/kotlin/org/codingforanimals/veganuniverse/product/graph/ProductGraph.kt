package org.codingforanimals.veganuniverse.product.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.codingforanimals.veganuniverse.product.categories.presentation.ProductCategoriesScreen
import org.codingforanimals.veganuniverse.product.list.presentation.ProductListScreen
import org.codingforanimals.veganuniverse.product.list.presentation.ProductListViewModel
import org.codingforanimals.veganuniverse.ui.navigation.Destination

sealed class ProductDestination(route: String) : Destination(route) {
    data object Categories : ProductDestination("product_categories_route")
    data object List : ProductDestination("product_list_route")
}

fun NavGraphBuilder.productGraph(
    navController: NavController,
) {
    composable(
        route = ProductDestination.Categories.route,
    ) {
        ProductCategoriesScreen(
            navigateToCategoryListScreen = { categoryName ->
                navController.navigate("${ProductDestination.List.route}/$categoryName")
            }
        )
    }

    composable(
        route = "${ProductDestination.List.route}/{${ProductListViewModel.CATEGORY_ARG}}",
        arguments = listOf(
            navArgument(ProductListViewModel.CATEGORY_ARG) {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        ProductListScreen(
            navigateUp = navController::navigateUp,
        )
    }
}