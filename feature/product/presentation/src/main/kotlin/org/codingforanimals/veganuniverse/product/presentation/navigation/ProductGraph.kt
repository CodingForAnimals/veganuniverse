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
import org.codingforanimals.veganuniverse.product.presentation.edit.EditProductScreen
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeScreen
import org.codingforanimals.veganuniverse.product.presentation.listing.ProductListingScreen
import org.codingforanimals.veganuniverse.product.presentation.navigation.ProductDestination.UnvalidatedDetail.Companion.UNVALIDATED
import org.codingforanimals.veganuniverse.product.presentation.validate.CompareProductEditScreen

sealed class ProductDestination(route: String) : Destination(route) {
    data object Home : ProductDestination("product_home_route")
    data class Browsing(
        val category: String? = null,
        val type: String? = null,
        val sorter: String? = null,
        val searchText: String = "",
    ) : ProductDestination("$ROUTE?$CATEGORY=$category&$TYPE=$type&$SEARCH_TEXT=$searchText") {
        companion object {
            const val ROUTE = "product_list_route"
            const val CATEGORY = "category"
            const val TYPE = "type"
            const val SEARCH_TEXT = "search_text"
        }
    }

    data class Detail(val id: String) : ProductDestination("$ROUTE?$ID_ARG=$id") {
        companion object {
            const val APP_LINK = "${DeepLink.APP_LINKS_BASE_URL}/product"
            const val ROUTE = "product_detail_route"
            const val ID_ARG = "product-id"
        }
    }

    data class UnvalidatedDetail(
        val id: String
    ) : ProductDestination("$ROUTE?$ID_ARG=$id&$UNVALIDATED=true") {
        companion object {
            const val ROUTE = "product_detail_route"
            const val ID_ARG = "product-id"
            const val UNVALIDATED = "unvalidated"
        }
    }

    data class Listing(val type: String) : ProductDestination("$ROUTE/$type") {
        companion object {
            const val ROUTE = "product_listing_route"
            const val TYPE = "type"
        }
    }

    data class Edit(val id: String?) :
        ProductDestination("$ROUTE?$ID=$id") {
        companion object {
            const val ROUTE = "edit_product"
            const val ID = "id"
        }
    }

    data class CompareEdit(val editId: String, val originalId: String) : ProductDestination(
        "$ROUTE?$EDIT_ID=$editId&$ORIGINAL_ID=$originalId"
    ) {
        companion object {
            const val EDIT_ID = "editId"
            const val ORIGINAL_ID = "originalId"
            const val ROUTE = "compare_edit"
        }
    }
}

fun NavGraphBuilder.productGraph(
    navController: NavController,
    navigateToAdditivesBrowsing: () -> Unit,
    navigateToThankYouScreen: () -> Unit,
) {
    composable(
        route = ProductDestination.Home.route,
    ) {
        ProductHomeScreen(
            navigateToCategoryListScreen = { category, type, sorter, searchText ->
                navController.navigate(
                    ProductDestination.Browsing(
                        category,
                        type,
                        sorter,
                        searchText
                    )
                )
            },
            navigateToProductDetail = { id ->
                navController.navigate(ProductDestination.Detail(id))
            },
            navigateToAdditivesBrowsing = navigateToAdditivesBrowsing,
        )
    }

    with(ProductDestination.Browsing) {
        composable(
            route = "$ROUTE?$CATEGORY={$CATEGORY}&$TYPE={$TYPE}&&$SEARCH_TEXT={$SEARCH_TEXT}",
            arguments = listOf(
                navArgument(CATEGORY) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(TYPE) {
                    type = NavType.StringType
                    nullable = true
                },
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
            route = "$ROUTE?$ID_ARG={$ID_ARG}&$UNVALIDATED={$UNVALIDATED}",
            arguments = listOf(
                navArgument(ID_ARG) {
                    type = NavType.StringType
                },
                navArgument(UNVALIDATED) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${DeepLink.APP_LINKS_BASE_URL}/product/{$ID_ARG}"
                }
            )
        ) {
            ProductDetailScreen(
                navigateUp = navController::navigateUp,
                navigateToEditProduct = {
                    navController.navigate(ProductDestination.Edit(id = it))
                }
            )
        }
    }

    with(ProductDestination.UnvalidatedDetail) {
        composable(
            route = "$ROUTE?$ID_ARG={$ID_ARG}&$UNVALIDATED=true",
            arguments = listOf(
                navArgument(ID_ARG) {
                    type = NavType.StringType
                },
                navArgument(UNVALIDATED) {
                    type = NavType.BoolType
                }
            ),
        ) {
            ProductDetailScreen(
                navigateUp = navController::navigateUp,
                navigateToEditProduct = {
                    navController.navigate(ProductDestination.Edit(id = it))
                }
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

    with(ProductDestination.Edit) {
        composable(
            route = "$ROUTE?$ID={$ID}",
            arguments = listOf(
                navArgument(ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = DeepLink.CreateProduct.deeplink
                }
            )
        ) {
            EditProductScreen(
                navigateUp = navController::navigateUp,
                navigateToThankYouScreen = navigateToThankYouScreen,
            )
        }
    }

    with(ProductDestination.CompareEdit) {
        composable(
            route = "$ROUTE?$EDIT_ID={$EDIT_ID}&$ORIGINAL_ID={$ORIGINAL_ID}",
        ) {
            CompareProductEditScreen(
                navigateUp = navController::navigateUp
            )
        }
    }
}
