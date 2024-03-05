@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.product.presentation.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.components.ProductRow
import org.codingforanimals.veganuniverse.product.presentation.components.ProductRowLoading
import org.codingforanimals.veganuniverse.product.presentation.components.ProductSuggestionDialog
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.Action
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.SideEffect
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.UiState
import org.codingforanimals.veganuniverse.product.ui.ProductType
import org.codingforanimals.veganuniverse.ui.R.string.unknown_error_message
import org.codingforanimals.veganuniverse.ui.Spacing_02
import org.codingforanimals.veganuniverse.ui.Spacing_03
import org.codingforanimals.veganuniverse.ui.Spacing_05
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.animation.shimmer
import org.codingforanimals.veganuniverse.ui.cards.VUCardDefaults
import org.codingforanimals.veganuniverse.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.ui.components.VUAssistChipDefaults
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.error.ErrorView
import org.codingforanimals.veganuniverse.ui.icon.VUIcons
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductHomeScreen(
    snackbarHostState: SnackbarHostState,
    navigateToCategoryListScreen: (String?) -> Unit,
    navigateToCreateProductScreen: () -> Unit,
    navigateToAuthScreen: () -> Unit,
    viewModel: ProductHomeViewModel = koinViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val latestProductsState by viewModel.latestProductsState.collectAsStateWithLifecycle()

    ProductHomeScreen(
        uiState = uiState,
        latestProductsState = latestProductsState,
        onAction = viewModel::onAction,
    )

    HandleSideEffects(
        sideEffects = viewModel.sideEffects,
        navigateToCategoryListScreen = navigateToCategoryListScreen,
        navigateToCreateProductScreen = navigateToCreateProductScreen,
        snackbarHostState = snackbarHostState,
        navigateToAuthScreen = navigateToAuthScreen,
    )
}

@Composable
private fun ProductHomeScreen(
    uiState: UiState,
    latestProductsState: ProductHomeViewModel.LatestProductsState,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(Spacing_06),
        verticalArrangement = Arrangement.spacedBy(Spacing_05),
    ) {
        item {
            Text(
                text = stringResource(R.string.categories_header_message),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        items(items = ProductType.values()) { type ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing_05),
            ) {
                VUIcon(icon = type.icon)
                Text(
                    text = stringResource(type.label),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(top = Spacing_03))
        }
        item {
            Row(
                modifier = Modifier.padding(bottom = Spacing_02),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.all_products),
                    style = MaterialTheme.typography.titleMedium,
                )
                TextButton(onClick = { onAction(Action.OnSeeAllClick) }) {
                    Text(
                        text = stringResource(R.string.see_all),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Crossfade(targetState = latestProductsState,
                label = "products_home_latest_products_crossfade",
                content = { state ->
                    when (state) {
                        ProductHomeViewModel.LatestProductsState.Error -> ErrorView(message = unknown_error_message)
                        ProductHomeViewModel.LatestProductsState.Loading -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shimmer()
                            ) {
                                ProductRowLoading()
                                ProductRowLoading()
                                ProductRowLoading()
                            }
                        }

                        is ProductHomeViewModel.LatestProductsState.Success -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outlineVariant,
                                        shape = RoundedCornerShape(Spacing_05),
                                    )
                                    .clip(RoundedCornerShape(Spacing_05)),
                            ) {
                                state.products.forEach { product ->
                                    key(product.id) {
                                        ProductRow(
                                            product = product,
                                            onImageClick = {
                                                onAction(
                                                    Action.ImageDialogAction.Open(
                                                        product.imageUrl
                                                    )
                                                )
                                            },
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

                    }
                })
        }
        item {
            Row(
                modifier = Modifier.padding(bottom = Spacing_02),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.categories),
                    style = MaterialTheme.typography.titleMedium,
                )
                TextButton(onClick = { onAction(Action.OnSeeAllClick) }) {
                    Text(
                        text = stringResource(R.string.see_all),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(
                    Spacing_06, Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(Spacing_06),
            ) {
                uiState.categories.forEach { category ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = { onAction(Action.OnProductCategorySelected(category)) },
                        elevation = VUCardDefaults.elevatedCardElevation(),
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentScale = ContentScale.Crop,
                            model = category.imageRef,
                            contentDescription = stringResource(category.label),
                        )
                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(Spacing_03),
                            text = stringResource(category.label),
                            maxLines = 2,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(top = Spacing_03))
        }
        item {
            Text(
                text = stringResource(R.string.add_product_suggestion),
                style = MaterialTheme.typography.bodyMedium
            )
            VUAssistChip(
                onClick = { onAction(Action.OnCreateProductClick) },
                label = stringResource(R.string.go_to_add_product),
                icon = VUIcons.ArrowForward,
                chipElevation = VUAssistChipDefaults.elevatedAssistChipElevation(),
            )
        }
    }

    uiState.imageUrl?.let { imageUrl ->
        Dialog(
            onDismissRequest = { onAction(Action.ImageDialogAction.Close) },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f),
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }

    uiState.productActionDialog?.let { productActionDialogType ->
        ProductSuggestionDialog(
            type = productActionDialogType,
            dismissDialog = { snackbarMessage: Int?, actionLabel: Int?, action: (suspend () -> Unit)? ->
                onAction(
                    Action.ProductSuggestionDialogAction.Dismiss(
                        snackbarMessage = snackbarMessage,
                        actionLabel = actionLabel,
                        action = action,
                    )
                )
            },
            navigateToAuthScreen = { onAction(Action.RelayAction.NavigateToAuthScreen) }
        )
    }
}

@Composable
private fun HandleSideEffects(
    sideEffects: Flow<SideEffect>,
    navigateToCategoryListScreen: (String?) -> Unit,
    navigateToCreateProductScreen: () -> Unit,
    navigateToAuthScreen: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateToCategoryListScreen -> {
                    navigateToCategoryListScreen(sideEffect.categoryName)
                }

                SideEffect.NavigateToCreateProductScreen -> navigateToCreateProductScreen()
                is SideEffect.ShowSnackbar -> {
                    when (snackbarHostState.showSnackbar(
                        message = context.getString(sideEffect.message),
                        duration = SnackbarDuration.Long,
                        actionLabel = sideEffect.actionLabel?.let { context.getString(it) },
                    )
                    ) {
                        SnackbarResult.Dismissed -> Unit
                        SnackbarResult.ActionPerformed -> coroutineScope.launch {
                            sideEffect.action?.invoke()
                        }
                    }
                }

                SideEffect.NavigateToAuthScreen -> navigateToAuthScreen()
            }
        }.collect()
    }
}
