package org.codingforanimals.veganuniverse.product.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.ui.topbar.HomeScreenTopAppBar
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.Action
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.NavigationEffect
import org.codingforanimals.veganuniverse.product.presentation.home.components.AllCategories
import org.codingforanimals.veganuniverse.product.presentation.home.components.FilterProductsByType
import org.codingforanimals.veganuniverse.product.presentation.home.components.ProductSearchBar
import org.codingforanimals.veganuniverse.product.presentation.home.components.SeeAdditivesBanner
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ProductHomeScreen(
    navigateToCategoryListScreen: (category: String?, type: String?, sorter: String?, searchText: String) -> Unit,
    navigateToProductDetail: (id: String) -> Unit,
    viewModel: ProductHomeViewModel = koinViewModel(),
) {

    ProductHomeScreen(
        searchText = viewModel.searchText,
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
    searchText: String?,
    onAction: (Action) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HomeScreenTopAppBar(
                title = stringResource(R.string.product_home_title),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier.padding(
                    bottom = Spacing_06,
                    start = Spacing_05,
                    end = Spacing_05,
                    top = Spacing_06
                ),
            ) {
                ProductSearchBar(
                    searchText = searchText.orEmpty(),
                    onTextChange = { onAction(Action.OnSearchTextChange(it)) },
                    onSearch = {
                        focusManager.clearFocus()
                        onAction(Action.OnSearchTextAction)
                    }
                )
                FilterProductsByType(
                    modifier = Modifier.padding(top = Spacing_04),
                    onShowAllClick = { onAction(Action.OnShowAllClick) },
                    onTypeClick = { onAction(Action.OnProductTypeClick(it)) }
                )
                SeeAdditivesBanner(
                    modifier = Modifier.padding(top = Spacing_06),
                    onClick = { onAction(Action.OnSeeAdditivesClick) },
                )
                AllCategories(
                    modifier = Modifier.padding(top = Spacing_04),
                    onShowMoreClick = { onAction(Action.OnShowAllClick) },
                    onItemClick = { onAction(Action.OnProductCategorySelected(it)) },
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationEffects(
    navigationEffects: Flow<NavigationEffect>,
    navigateToCategoryListScreen: (category: String?, type: String?, sorter: String?, searchText: String) -> Unit,
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
                        effect.searchText.orEmpty(),
                    )
                }

                is NavigationEffect.NavigateToProductDetail -> navigateToProductDetail(effect.id)
            }
        }.collect()
    }
}
