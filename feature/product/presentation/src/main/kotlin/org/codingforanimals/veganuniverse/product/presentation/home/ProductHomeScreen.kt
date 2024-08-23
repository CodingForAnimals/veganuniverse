package org.codingforanimals.veganuniverse.product.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.Action
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.NavigationEffect
import org.codingforanimals.veganuniverse.product.presentation.home.components.AllCategories
import org.codingforanimals.veganuniverse.product.presentation.home.components.LatestProductsCards
import org.codingforanimals.veganuniverse.product.presentation.home.components.ProductsByType
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ProductHomeScreen(
    navigateToCategoryListScreen: (category: String?, type: String?, sorter: String?) -> Unit,
    navigateToProductDetail: (id: String) -> Unit,
    viewModel: ProductHomeViewModel = koinViewModel(),
) {

    val latestProductsState by viewModel.latestProductsState.collectAsStateWithLifecycle()

    ProductHomeScreen(
        latestProductsState = latestProductsState,
        onAction = viewModel::onAction,
    )

    HandleNavigationEffects(
        navigationEffects = viewModel.navigationEffects,
        navigateToCategoryListScreen = navigateToCategoryListScreen,
        navigateToProductDetail = navigateToProductDetail,
    )
}

@Composable
private fun ProductHomeScreen(
    latestProductsState: ProductHomeViewModel.LatestProductsState,
    onAction: (Action) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier.padding(
                bottom = Spacing_06,
                start = Spacing_05,
                end = Spacing_05,
            ),
        ) {
            ProductsByType(
                onShowAllClick = { onAction(Action.OnShowAllClick) },
                onTypeClick = { onAction(Action.OnProductTypeClick(it)) }
            )
            LatestProductsCards(
                modifier = Modifier.padding(top = Spacing_05),
                latestProductsState = latestProductsState,
                onShowMoreClick = { onAction(Action.OnMostRecentShowMoreClick) },
                onProductClick = { onAction(Action.OnProductClick(it)) }
            )
            AllCategories(
                modifier = Modifier.padding(top = Spacing_05),
                onShowMoreClick = { onAction(Action.OnShowAllClick) },
                onItemClick = { onAction(Action.OnProductCategorySelected(it)) },
            )
        }
    }
}

@Composable
private fun HandleNavigationEffects(
    navigationEffects: Flow<NavigationEffect>,
    navigateToCategoryListScreen: (category: String?, type: String?, sorter: String?) -> Unit,
    navigateToProductDetail: (id: String) -> Unit,
) {
    LaunchedEffect(Unit) {
        navigationEffects.onEach { effect ->
            when (effect) {
                is NavigationEffect.NavigateToProductBrowsing -> {
                    navigateToCategoryListScreen(
                        effect.category?.name,
                        effect.type?.name,
                        effect.sorter?.name,
                    )
                }

                is NavigationEffect.NavigateToProductDetail -> navigateToProductDetail(effect.id)
            }
        }.collect()
    }
}
