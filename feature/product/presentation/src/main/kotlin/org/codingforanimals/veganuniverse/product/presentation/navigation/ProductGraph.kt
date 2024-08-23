package org.codingforanimals.veganuniverse.product.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.ui.navigation.Destination
import org.codingforanimals.veganuniverse.commons.ui.navigation.navigate
import org.codingforanimals.veganuniverse.product.presentation.browsing.ProductBrowsingScreen
import org.codingforanimals.veganuniverse.product.presentation.detail.ProductDetailScreen
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeScreen

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
}

fun NavGraphBuilder.productGraph(
    navController: NavController,
    navigateToCreateProductScreen: () -> Unit,
    navigateToAuthenticateScreen: () -> Unit,
    navigateToAuthScreen: (ProductDestination) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    composable(
        route = ProductDestination.Home.route,
    ) {
        ProductHomeScreen(
            snackbarHostState = snackbarHostState,
            navigateToCategoryListScreen = { category, type, sorter ->
                navController.navigate(ProductDestination.Browsing(category, type, sorter))
            },
            navigateToCreateProductScreen = navigateToCreateProductScreen,
            navigateToAuthScreen = { navigateToAuthScreen(ProductDestination.Home) },
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
                navigateToAuthScreen = navigateToAuthenticateScreen,
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
            )
        ) {
            ProductDetailScreen(
                navigateUp = navController::popBackStack,
                navigateToAuthenticateScreen = navigateToAuthenticateScreen,
            )
        }
    }
}

class PepeViewModel(
) : ViewModel() {
    private val searchChannel = Channel<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val products: Flow<PagingData<Int>> =
        searchChannel.receiveAsFlow().map {
            PagingData.from(listOf(2, 3))
//            val params = ProductQueryParams.Builder().build()
//            productRepository.queryProductsPagingDataFlow(params).cachedIn(viewModelScope)
//                .map { pagingData ->
//                    pagingData.map { model -> model.toView() }
//                }
        }

    init {
        viewModelScope.launch {
            searchChannel.send(Unit)
        }
    }
}