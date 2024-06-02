@file:OptIn(ExperimentalLayoutApi::class)

package org.codingforanimals.veganuniverse.product.presentation.home

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.components.ProductSuggestionDialog
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.Action
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.SideEffect
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.UiState
import org.codingforanimals.veganuniverse.product.ui.ProductType
import org.codingforanimals.veganuniverse.ui.Spacing_02
import org.codingforanimals.veganuniverse.ui.Spacing_03
import org.codingforanimals.veganuniverse.ui.Spacing_05
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.cards.VUCardDefaults
import org.codingforanimals.veganuniverse.ui.components.VUAssistChip
import org.codingforanimals.veganuniverse.ui.components.VUAssistChipDefaults
import org.codingforanimals.veganuniverse.ui.components.VUIcon
import org.codingforanimals.veganuniverse.ui.icon.VUIcons
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductHomeScreen(
    snackbarHostState: SnackbarHostState,
    navigateToCategoryListScreen: (category: String?, type: String?, sorter: String?) -> Unit,
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
            LatestProducts(
                latestProductsState = latestProductsState,
                onShowMoreClick = { onAction(Action.OnMostRecentShowMoreClick) },
                onImageClick = { onAction(Action.ImageDialogAction.Open(it)) },
                onEditClick = { onAction(Action.ProductSuggestionDialogAction.OpenEdit(it)) },
                onReportClick = { onAction(Action.ProductSuggestionDialogAction.OpenReport(it)) }
            )
        }
        item {
            AllCategories(
                onShowMoreClick = { onAction(Action.OnMostRecentShowMoreClick) },
                categories = uiState.categories,
                onItemClick = { onAction(Action.OnProductCategorySelected(it)) },
            )
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(top = Spacing_03))
        }
        item {
            CreateProductCTA(
                onButtonClick = { onAction(Action.OnCreateProductClick) },
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
    navigateToCategoryListScreen: (category: String?, type: String?, sorter: String?) -> Unit,
    navigateToCreateProductScreen: () -> Unit,
    navigateToAuthScreen: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        sideEffects.onEach { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateToProductBrowsing -> {
                    navigateToCategoryListScreen(
                        sideEffect.category?.name,
                        sideEffect.type?.name,
                        sideEffect.sorter?.name,
                    )
                }

                SideEffect.NavigateToCreateProduct -> navigateToCreateProductScreen()
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

                SideEffect.NavigateToAuthentication -> navigateToAuthScreen()
            }
        }.collect()
    }
}
