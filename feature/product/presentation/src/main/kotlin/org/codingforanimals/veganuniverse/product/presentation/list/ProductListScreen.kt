@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.product.presentation.list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ShapeDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.components.ProductRow
import org.codingforanimals.veganuniverse.product.presentation.components.ProductSuggestionDialog
import org.codingforanimals.veganuniverse.product.presentation.list.ProductListViewModel.Action
import org.codingforanimals.veganuniverse.product.presentation.list.ProductListViewModel.SideEffect
import org.codingforanimals.veganuniverse.product.presentation.list.ProductListViewModel.UiState
import org.codingforanimals.veganuniverse.product.presentation.model.Product
import org.codingforanimals.veganuniverse.product.ui.ProductCategory
import org.codingforanimals.veganuniverse.product.ui.ProductType
import org.codingforanimals.veganuniverse.ui.R.drawable.ic_search
import org.codingforanimals.veganuniverse.ui.Spacing_02
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_05
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.components.SelectableChip
import org.codingforanimals.veganuniverse.ui.components.VUCircularProgressIndicator
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.components.VUTopAppBar
import org.codingforanimals.veganuniverse.ui.icon.VUIcons
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductListScreen(
    navigateUp: () -> Unit,
    navigateToAuthScreen: () -> Unit,
    viewModel: ProductListViewModel = koinViewModel(),
) {
    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateUp = navigateUp,
        navigateToAuthScreen = navigateToAuthScreen,
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val products = viewModel.products.collectAsLazyPagingItems()
    ProductListScreen(
        uiState = uiState,
        products = products,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun ProductListScreen(
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

    var imageDialogUrl by rememberSaveable { mutableStateOf("") }
    var imageDialogVisible by rememberSaveable { mutableStateOf(false) }
    fun showImageDialog(url: String) {
        imageDialogUrl = url
        imageDialogVisible = true
    }

    Column {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            VUTopAppBar(
                title = stringResource(
                    uiState.category?.label ?: R.string.product_list_default_title
                ),
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
                    maxLines = 1,
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

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(products.itemCount) { index ->
                val product = products[index] ?: return@items
                key(product.id) {
                    ProductRow(
                        product = product,
                        onImageClick = { product.imageUrl?.let { showImageDialog(it) } },
                        onEditClick = {
                            onAction(
                                Action.ProductSuggestionDialogAction.OpenEdit(
                                    product
                                )
                            )
                        },
                        onReportClick = {
                            onAction(
                                Action.ProductSuggestionDialogAction.OpenReport(
                                    product
                                )
                            )
                        },
                    )
                }
            }
        }
    }

    if (imageDialogVisible) {
        Dialog(
            onDismissRequest = { imageDialogVisible = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f),
                model = imageDialogUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }

    if (bottomSheetVisible) {
        ModalBottomSheet(
            sheetState = modalBottomSheetState,
            onDismissRequest = { bottomSheetVisible = false },
            content = {
                var currentCategory by rememberSaveable { mutableStateOf(uiState.category) }
                var currentType by rememberSaveable { mutableStateOf(uiState.filterType) }
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing_04),
                    contentPadding = PaddingValues(Spacing_06)
                ) {
                    item {
                        Text(
                            text = stringResource(org.codingforanimals.veganuniverse.ui.R.string.filter_by),
                            style = MaterialTheme.typography.titleLarge,
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing_04))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalArrangement = Arrangement.spacedBy(Spacing_02),
                        ) {
                            ProductCategory.values().forEach {
                                key(it.name.hashCode()) {
                                    SelectableChip(
                                        label = stringResource(it.label),
                                        selected = currentCategory == it,
                                        onClick = {
                                            currentCategory =
                                                if (currentCategory == it) null else it
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
                        ) {
                            ProductType.values().forEach {
                                key(it.name.hashCode()) {
                                    SelectableChip(
                                        label = stringResource(it.label),
                                        selected = currentType == it,
                                        onClick = {
                                            currentType = if (currentType == it) null else it
                                        },
                                    )
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing_04))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = Spacing_06)
                        ) {
                            TextButton(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    currentCategory = null
                                    currentType = null
                                    onAction(Action.OnClearFiltersClick)
                                    dismissFiltersSheet()
                                }) {
                                Text(text = "Limpiar filtros")
                            }
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onAction(Action.ApplyFiltersClick(currentCategory, currentType))
                                    dismissFiltersSheet()
                                }) {
                                Text(text = "Aplicar filtros")
                            }
                        }
                    }
                }
            })
    }

    products.loadState.apply {
        when {
            refresh is LoadState.Loading -> VUCircularProgressIndicator()
            refresh is LoadState.Error -> Log.e(
                "ProductListScreen",
                "Error refreshing items ${(refresh as? LoadState.Error)?.error}",
            )

            append is LoadState.Loading -> VUCircularProgressIndicator()
            append is LoadState.Error -> Log.e(
                "ProductListScreen",
                "Error appending items ${(append as? LoadState.Error)?.error}",
            )
        }
    }

    uiState.productSuggestionType?.let { productActionDialogType ->
        ProductSuggestionDialog(
            type = productActionDialogType,
            dismissDialog = { _, _, _ -> onAction(Action.ProductSuggestionDialogAction.Close) },
            navigateToAuthScreen = { onAction(Action.RelayAction.NavigateToAuthScreen) },
        )
    }
}


@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateUp: () -> Unit,
    navigateToAuthScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                SideEffect.NavigateUp -> {
                    navigateUp()
                }

                SideEffect.NavigateToAuthScreen -> navigateToAuthScreen()
            }
        }.collect()
    }
}