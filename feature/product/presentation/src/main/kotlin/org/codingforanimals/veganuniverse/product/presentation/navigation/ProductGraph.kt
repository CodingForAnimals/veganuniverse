package org.codingforanimals.veganuniverse.product.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import org.codingforanimals.veganuniverse.commons.navigation.DeepLink
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.commons.ui.navigation.navigate
import org.codingforanimals.veganuniverse.product.presentation.browsing.ProductBrowsingScreen
import org.codingforanimals.veganuniverse.product.presentation.detail.ProductDetailScreen
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeScreen
import org.codingforanimals.veganuniverse.product.presentation.listing.ProductListingScreen

sealed class ProductDestination(route: String) : Destination(route) {
    data object Home : ProductDestination("product_home_route")
    data class Browsing(
        val category: String? = null,
        val type: String? = null,
        val sorter: String? = null
    ) : ProductDestination("$ROUTE?$CATEGORY=$category&$TYPE=$type&$SORTER=$sorter") {
        companion object {
            const val ROUTE = "product_list_route"
            const val CATEGORY = "category"
            const val TYPE = "type"
            const val SORTER = "sorter"
        }
    }

    data class Detail(val id: String) : ProductDestination("$ROUTE/$id") {
        companion object {
            const val ROUTE = "product_detail_route"
            const val ID_ARG = "product-id"
        }
    }

    data class Listing(val type: String) : ProductDestination("$ROUTE/$type") {
        companion object {
            const val ROUTE = "product_listing_route"
            const val TYPE = "type"
        }
    }
}

fun NavGraphBuilder.productGraph(
    navController: NavController,
) {
    composable(
        route = ProductDestination.Home.route,
    ) {
        ProductHomeScreen(
            navigateToCategoryListScreen = { category, type, sorter ->
                navController.navigate(ProductDestination.Browsing(category, type, sorter))
            },
            navigateToProductDetail = { id ->
                navController.navigate(ProductDestination.Detail(id))
            },
        )
    }

    with(ProductDestination.Browsing) {
        composable(
            route = "$ROUTE?$CATEGORY={$CATEGORY}&$TYPE={$TYPE}&$SORTER={$SORTER}",
            arguments = listOf(
                navArgument(CATEGORY) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(TYPE) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(SORTER) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            ProductBrowsingScreen(
                navigateUp = navController::navigateUp,
                navigateToProductDetail = {
                    navController.navigate(ProductDestination.Detail(it))
                }
            )
        }
    }

    with(ProductDestination.Detail) {
        composable(
            route = "$ROUTE/{$ID_ARG}",
            arguments = listOf(
                navArgument(ID_ARG) {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DeepLink.ProductDetail.pathWithSchema}/{$ID_ARG}"
                }
            )
        ) {
            ProductDetailScreen(
                navigateUp = navController::popBackStack,
            )
        }
    }

    with(ProductDestination.Listing) {
        composable(
            route = "$ROUTE/{$TYPE}",
            arguments = listOf(
                navArgument(TYPE) {
                    type = NavType.StringType
                }
            )
        ) {
            ProductListingScreen(
                navigateUp = navController::navigateUp,
                onProductClick = { id ->
                    navController.navigate(ProductDestination.Detail(id))
                }
            )
        }
    }
}
