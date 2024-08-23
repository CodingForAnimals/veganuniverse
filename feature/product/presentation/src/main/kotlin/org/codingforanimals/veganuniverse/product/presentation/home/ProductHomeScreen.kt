package org.codingforanimals.veganuniverse.product.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_03
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_05
import org.codingforanimals.veganuniverse.commons.designsystem.Spacing_06
import org.codingforanimals.veganuniverse.commons.product.presentation.toUI
import org.codingforanimals.veganuniverse.commons.product.shared.model.ProductType
import org.codingforanimals.veganuniverse.commons.ui.components.VUIcon
import org.codingforanimals.veganuniverse.commons.ui.contribution.ReportContentDialog
import org.codingforanimals.veganuniverse.commons.ui.contribution.EditContentDialog
import org.codingforanimals.veganuniverse.commons.ui.snackbar.HandleSnackbarEffects
import org.codingforanimals.veganuniverse.commons.user.presentation.UnverifiedEmailDialog
import org.codingforanimals.veganuniverse.product.presentation.R
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.Action
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.NavigationEffect
import org.codingforanimals.veganuniverse.product.presentation.home.ProductHomeViewModel.UiState
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

    HandleNavigationEffects(
        navigationEffects = viewModel.navigationEffects,
        navigateToCategoryListScreen = navigateToCategoryListScreen,
        navigateToCreateProductScreen = navigateToCreateProductScreen,
        navigateToAuthScreen = navigateToAuthScreen,
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
    uiState: UiState,
    latestProductsState: ProductHomeViewModel.LatestProductsState,
    onAction: (Action) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = Spacing_06,
            horizontal = Spacing_05,
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing_05),
    ) {
        item {
            Text(
                text = stringResource(R.string.categories_header_message),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        items(items = ProductType.entries) { type ->
            val typeUI = remember { type.toUI() }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing_05),
            ) {
                VUIcon(icon = typeUI.icon)
                Text(
                    text = stringResource(typeUI.label),
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
                onEditClick = { onAction(Action.OpenSuggestDialog(it)) },
                onReportClick = { onAction(Action.OpenReportDialog(it)) }
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
}

@Composable
private fun HandleNavigationEffects(
    navigationEffects: Flow<NavigationEffect>,
    navigateToCategoryListScreen: (category: String?, type: String?, sorter: String?) -> Unit,
    navigateToCreateProductScreen: () -> Unit,
    navigateToAuthScreen: () -> Unit,
) {
    LaunchedEffect(Unit) {
        navigationEffects.onEach { sideEffect ->
            when (sideEffect) {
                is NavigationEffect.NavigateToProductBrowsing -> {
                    navigateToCategoryListScreen(
                        sideEffect.category?.name,
                        sideEffect.type?.name,
                        sideEffect.sorter?.name,
                    )
                }

                NavigationEffect.NavigateToCreateProduct -> navigateToCreateProductScreen()
                NavigationEffect.NavigateToAuthentication -> navigateToAuthScreen()
            }
        }.collect()
    }
}
