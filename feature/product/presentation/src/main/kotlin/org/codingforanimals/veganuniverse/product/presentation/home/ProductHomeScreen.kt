package org.codingforanimals.veganuniverse.product.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialog
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialog
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailDialog
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.Action
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.NavigationEffect
import org.codingforanimals.veganuniverse.product.presentation.home.components.AllCategories
import org.codingforanimals.veganuniverse.product.presentation.home.components.LatestProductsCards
import org.codingforanimals.veganuniverse.product.presentation.home.components.ProductsByType
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ProductHomeScreen(
    snackbarHostState: SnackbarHostState,
    navigateToCategoryListScreen: (category: String?, type: String?, sorter: String?) -> Unit,
    navigateToAuthScreen: () -> Unit,
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
        navigateToAuthScreen = navigateToAuthScreen,
        navigateToProductDetail = navigateToProductDetail,
    )

    viewModel.showReportDialog?.let {
        ReportContentDialog(
            onResult = viewModel::onReportResult
        )
    }

    viewModel.showSuggestionDialog?.let {
        EditContentDialog(
            onResult = viewModel::onEditResult
        )
    }

    if (viewModel.showUnverifiedEmailDialog) {
        UnverifiedEmailDialog(onResult = viewModel::onUnverifiedEmailResult)
    }

    HandleSnackbarEffects(
        snackbarEffects = viewModel.snackbarEffects,
        snackbarHostState = snackbarHostState,
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
                bottom = Spacing_05,
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
                onShowMoreClick = { onAction(Action.OnMostRecentShowMoreClick) },
                onItemClick = { onAction(Action.OnProductCategorySelected(it)) },
            )
            HorizontalDivider(modifier = Modifier.padding(top = Spacing_03))
        }
    }
}

@Composable
private fun HandleNavigationEffects(
    navigationEffects: Flow<NavigationEffect>,
    navigateToCategoryListScreen: (category: String?, type: String?, sorter: String?) -> Unit,
    navigateToAuthScreen: () -> Unit,
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

                NavigationEffect.NavigateToAuthentication -> navigateToAuthScreen()
                is NavigationEffect.NavigateToProductDetail -> navigateToProductDetail(effect.id)
            }
        }.collect()
    }
}
