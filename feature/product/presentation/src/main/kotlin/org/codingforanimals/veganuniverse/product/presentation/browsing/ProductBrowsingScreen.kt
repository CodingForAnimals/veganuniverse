@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.product.presentation.browsing

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_02
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_04
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_08
import org.codingforanimals.veganuniverse.commons.product.presentation.label
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductCategory
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductSorter
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.R.drawable.ic_search
import org.codingforanimals.veganuniverse.commons.ui.R.string.filter_by
import org.codingforanimals.veganuniverse.commons.ui.components.SelectableChip
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.commons.ui.error.ErrorView
import org.codingforanimals.veganuniverse.commons.ui.icon.VUIcons
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.browsing.ProductBrowsingViewModel.Action
import org.codingforanimals.veganuniverse.product.presentation.browsing.ProductBrowsingViewModel.NavigationEffect
import org.codingforanimals.veganuniverse.product.presentation.browsing.ProductBrowsingViewModel.UiState
import org.codingforanimals.veganuniverse.product.presentation.components.ProductCard
import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ProductBrowsingScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateToProductDetail: (String) -> Unit,
) {
    val viewModel: ProductBrowsingViewModel = koinViewModel()
    val products = viewModel.products.collectAsLazyPagingItems()

    val snackbarHostState = remember { SnackbarHostState() }
    ProductBrowsingScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        uiState = viewModel.uiState,
        products = products,
        onAction = viewModel::onAction,
    )

    HandleNavigationEffects(
        navigationEffects = viewModel.navigationEffects,
        navigateUp = navigateUp,
        navigateToProductDetail = navigateToProductDetail,
    )

    HandleSnackbarEffects(
        snackbarEffects = viewModel.snackbarEffects,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun ProductBrowsingScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    uiState: UiState,
    products: LazyPagingItems<Product>,
    onAction: (Action) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var bottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    fun openFiltersSheet() {
        bottomSheetVisible = true
    }

    fun dismissFiltersSheet() {
        coroutineScope.launch { modalBottomSheetState.hide() }.invokeOnCompletion {
            if (!modalBottomSheetState.isVisible) {
                bottomSheetVisible = false
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                VUTopAppBar(
                    title = {},
                    onBackClick = { onAction(Action.OnBackClick) },
                )
                Row(
                    modifier = Modifier.padding(
                        bottom = Spacing_04,
                        start = Spacing_05,
                        end = Spacing_05,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(Spacing_04),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = uiState.searchText,
                        onValueChange = { onAction(Action.OnSearchTextChange(it)) },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = ic_search),
                                contentDescription = null,
                            )
                        },
                        shape = ShapeDefaults.Medium,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        placeholder = { Text(text = stringResource(id = R.string.search_product)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        )
                    )
                    IconButton(
                        onClick = ::openFiltersSheet,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary,
                        ),
                        content = {
                            VUIcon(
                                icon = VUIcons.Filter,
                                contentDescription = ""
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Crossfade(
            modifier = Modifier.padding(paddingValues),
            targetState = products.itemCount == 0 && products.loadState.refresh !is LoadState.Loading && products.loadState.append !is LoadState.Loading,
            label = "products_empty_state_cross_fade",
        ) { isEmpty ->
            if (isEmpty) {
                ErrorView(message = R.string.no_products_found)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Spacing_05),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(
                        start = Spacing_05,
                        end = Spacing_05,
                        top = Spacing_05,
                        bottom = Spacing_08,
                    )
                ) {
                    items(products.itemCount) { index ->
                        val product = products[index] ?: return@items
                        key(product.id) {
                            ProductCard(
                                product = product,
                                onClick = { product.id?.let { onAction(Action.OnProductClick(it)) } },
                            )
                        }
                    }

                    products.loadState.apply {
                        when {
                            refresh is LoadState.Loading -> item { CircularProgressIndicator() }
                            append is LoadState.Loading -> item { CircularProgressIndicator() }
                        }
                    }
                }
            }
        }
    }

    if (bottomSheetVisible) {
        ModalBottomSheet(
            modifier = Modifier.padding(top = Spacing_04),
            sheetState = modalBottomSheetState,
            onDismissRequest = { bottomSheetVisible = false }
        ) {
            var currentCategory by rememberSaveable { mutableStateOf(uiState.category) }
            var currentType by rememberSaveable { mutableStateOf(uiState.type) }
            var currentSorter by rememberSaveable { mutableStateOf(uiState.sorter) }
            LazyColumn(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing_04),
                contentPadding = PaddingValues(Spacing_03)
            ) {
                item {
                    Text(
                        text = stringResource(R.string.order_by),
                        style = MaterialTheme.typography.titleLarge,
                    )

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Spacing_03),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalArrangement = Arrangement.spacedBy(Spacing_02),
                    ) {
                        ProductSorter.entries.forEach { sorter ->
                            val label = remember { sorter.label }
                            SelectableChip(
                                label = stringResource(label),
                                selected = currentSorter == sorter,
                                onClick = { currentSorter = sorter },
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = Spacing_04))

                    Text(
                        text = stringResource(filter_by),
                        style = MaterialTheme.typography.titleLarge,
                    )

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Spacing_03),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalArrangement = Arrangement.spacedBy(Spacing_02),
                        maxItemsInEachRow = 2,
                    ) {
                        ProductType.entries.forEach {
                            key(it.name.hashCode()) {
                                val typeUI = remember { it.toUI() }
                                SelectableChip(
                                    label = stringResource(typeUI.label),
                                    selected = currentType == it,
                                    onClick = {
                                        currentType = if (currentType == it) null else it
                                    },
                                )
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = Spacing_04))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalArrangement = Arrangement.spacedBy(Spacing_02),
                        maxItemsInEachRow = 2,
                    ) {
                        ProductCategory.entries.forEach {
                            key(it.name.hashCode()) {
                                val categoryUI = remember { it.toUI() }
                                SelectableChip(
                                    label = stringResource(categoryUI.label),
                                    selected = currentCategory == it,
                                    onClick = {
                                        currentCategory =
                                            if (currentCategory == it) null else it
                                    },
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing_06)
            ) {
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        currentCategory = null
                        currentType = null
                        onAction(Action.OnClearFiltersClick)
                        dismissFiltersSheet()
                    }) {
                    Text(text = stringResource(R.string.clear_filters))
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onAction(
                            Action.ApplyFiltersClick(
                                currentCategory,
                                currentType,
                                currentSorter
                            )
                        )
                        dismissFiltersSheet()
                    }) {
                    Text(text = stringResource(R.string.apply_filters))
                }
            }
        }
    }
}


@Composable
private fun HandleNavigationEffects(
    navigationEffects: Flow<NavigationEffect>,
    navigateUp: () -> Unit,
    navigateToProductDetail: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        navigationEffects.onEach { sideEffect ->
            when (sideEffect) {
                NavigationEffect.NavigateUp -> {
                    navigateUp()
                }

                is NavigationEffect.NavigateToProductDetails -> {
                    navigateToProductDetail(sideEffect.id)
                }
            }
        }.collect()
    }
}
